package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import com.google.gson.annotations.*;

import java.util.ArrayList;
import java.util.Comparator;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.ColorHolder;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Data.TileMovementProvider;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.FromTo;
import de.hsd.hacking.Utils.RandomIntPool;
import de.hsd.hacking.Utils.Shader;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class Employee extends Entity implements Comparable<Employee>, Touchable {

    private ShapeRenderer debugRenderer;

    private final int BODY = 0;
    private final int HAIR = 1;
    private boolean touched;
    private int touchTintFrames = 0;


    public enum EmployeeSkillLevel {
        NOOB, INTERMEDIATE, PRO, WIZARD;

        private static final EmployeeSkillLevel[] VALUES = values();
        public static final int SIZE = VALUES.length;

        public static EmployeeSkillLevel getRandomSkillLevel() { return VALUES[MathUtils.random(SIZE - 1)]; }
    }

    public enum HairStyle {
        CRAZY, NEAT, NERD, RASTA;
    }

    //Graphics
    private Assets assets;
    private Animation[][] animations;
    enum AnimState{
        IDLE, MOVING
    }
    private ShaderProgram shader;
    private AnimState animationState;
    private boolean flipped;

    //Data
    @Expose private String surName;
    @Expose private String lastName;
    @Expose private String description; // ? Needed ?
    @Expose private EmployeeSkillLevel skillLevel;
    @Expose private ArrayList<Skill> skillSet;
    private float elapsedTime = 0f;
    private TileMovementProvider movementProvider;
    @Expose private HairStyle hairStyle;
    @Expose private Color hairColor, eyeColor, skinColor, shirtColor, trouserColor, shoeColor;
    @Expose private EmployeeState state;
    private Rectangle bounds;



    public Employee() {
        super(null, false);
    }

    /**
     * Creates a new random employee
     * @param level The desired skill Level
     */
    public Employee(Assets assets, EmployeeSkillLevel level, TileMovementProvider movementProvider, GameStage stage){
        super(stage, false);
        this.assets = assets;

        //Create random name
        String[] randomName = DataLoader.getInstance().getNewName();
        this.surName = randomName[0];
        this.lastName = randomName[1];
        this.skillLevel = level;

        //Skill points to spend. NOOB = 55, INTERMEDIATE = 65, PRO = 75, WIZARD = 85
        //35 Points are spent by default (5 per Skill)
        int skillPoints = 55 + skillLevel.ordinal() * 10;
        skillSet = new ArrayList<Skill>(7);
        for (SkillType type :
                SkillType.values()) {
            skillSet.add(new Skill(type, 5));
            skillPoints -= 5;
        }
        //RandomIntPool chooses a number randomly from a set of predefined numbers.
        //Used numbers can either be removed or left in the set.
        RandomIntPool pool = new RandomIntPool(new FromTo(0, skillSet.size() - 1));

        while (skillPoints > 0){
            int randomInt = pool.getRandomNumberWithoutRemoving();
            Skill incrementSkill = skillSet.get(randomInt);
            if (incrementSkill.getValue() < 15){
                incrementSkill.incrementSkill();
                skillPoints--;
            }else{ //Skill at max -> Remove skill index from possible random indexes
                pool.removeNumber(randomInt);
            }
        }
        this.movementProvider = movementProvider;
        this.position = movementProvider.getStartPosition(this);
        this.animationState = AnimState.IDLE;
        this.state = new IdleState(this);
        //Graphics
        setUpAnimations();
        setUpShader();
        this.bounds = new Rectangle(position.x + 5f, position.y + 5f, 22f, 45f); //values measured from sprite
        debugRenderer = new ShapeRenderer();

    }

    private void setUpShader() {
        String vertexShader = Shader.vertexShader;
        String fragmentShader = Shader.getFragmentShader(
                Color.valueOf(ColorHolder.HairColors.get(MathUtils.random(ColorHolder.HairColors.size() - 1))),
                Color.valueOf(ColorHolder.SkinColors.get(MathUtils.random(ColorHolder.SkinColors.size() - 1))),
                Color.valueOf(ColorHolder.ShirtColors.get(MathUtils.random(ColorHolder.ShirtColors.size() - 1))),
                Color.valueOf(ColorHolder.TrouserColors.get(MathUtils.random(ColorHolder.TrouserColors.size() - 1))),
                Color.valueOf(ColorHolder.EyesColors.get(MathUtils.random(ColorHolder.EyesColors.size() - 1))),
                Color.valueOf(ColorHolder.ShoesColors.get(MathUtils.random(ColorHolder.ShoesColors.size() - 1))));
        this.shader = new ShaderProgram(vertexShader, fragmentShader);
    }


    //GETTER & SETTER

    public Vector2 getPosition() {
        return position;
    }
    public TileMovementProvider getMovementProvider() {
        return movementProvider;
    }
    public AnimState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimState animationState) {
        this.animationState = animationState;
    }
    public void flipHorizontal(boolean toRight){
        this.flipped = toRight;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setShader(shader);
        if (touchTintFrames > 0){
            batch.setColor(Color.RED);
        }
        for (int i = 0; i < 2; i++) {
            TextureRegion frame = animations[animationState.ordinal()][i].getKeyFrame(elapsedTime, true);
            batch.draw(frame, flipped ? this.position.x + frame.getRegionWidth() : this.position.x, this.position.y, flipped ? -frame.getRegionWidth() : frame.getRegionWidth(), frame.getRegionHeight());
        }
        batch.setShader(null);
        if (touchTintFrames > 0){
            batch.setColor(Color.WHITE);
        }
        if (Constants.DEBUG){
            batch.end();
            debugRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            debugRenderer.setTransformMatrix(batch.getTransformMatrix());
            debugRenderer.setColor(Color.GREEN);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            debugRenderer.end();
            batch.begin();
        }
        assets.gold_font_small.draw(batch, getName(), position.x - 30f, position.y + 70f, 92f, Align.center, false);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        if (touchTintFrames > 0){
            touchTintFrames--;
        }
        EmployeeState state = this.state.act(delta);
        if (state != null){
            this.state.leave();
            this.state = state;
            this.state.enter();
        }
    }

    @Override
    public String toString(){
        return "Employee: " + surName + " " + lastName + ". Skilllevel: " + this.skillLevel.name() + ". Skills: " + skillSetToString();
    }

    public void setPosition(Vector2 position){
        this.position.set(position);
        this.bounds.setPosition(position.cpy().add(5f, 5f));
    }

    private String skillSetToString(){
        String skills = "";
        for (Skill skill :
                skillSet) {
            skills += skill.getType().name() + " : " + skill.getValue() + "; ";
        }
        return skills;
    }

    @Override
    public String getName(){
        return surName + " " +lastName;
    }

    void resetElapsedTime() {
        this.elapsedTime = 0f;
    }

    private void setUpAnimations() {
        this.animations = new Animation[AnimState.values().length][2];
        boolean hair1 = MathUtils.randomBoolean();
        animations[AnimState.MOVING.ordinal()][BODY] = new Animation(.5f, assets.gray_character_body.get(0), assets.gray_character_body.get(1));
        animations[AnimState.MOVING.ordinal()][HAIR] = new Animation(.5f, hair1 ? assets.hair_01.get(0) : assets.hair_02.get(0), hair1 ? assets.hair_01.get(1) : assets.hair_02.get(1));

        animations[AnimState.IDLE.ordinal()][BODY] = new Animation(.5f, assets.gray_character_body.get(2), assets.gray_character_body.get(3));
        animations[AnimState.IDLE.ordinal()][HAIR] = new Animation(.5f, hair1 ? assets.hair_01.get(2) : assets.hair_02.get(2), hair1 ? assets.hair_01.get(3) : assets.hair_02.get(3));

    }

    @Override
    public void touchDown(Vector2 position) {
        if (bounds.contains(position)){
            touched = true;
        }
    }

    @Override
    public void touchUp(Vector2 position) {
        if (bounds.contains(position) && touched){
            touchTintFrames += 30;
        }
        touched = false;
    }

    @Override
    public void touchDragged(Vector2 position) {

    }


    @Override
    public int compareTo(Employee o) {
        if (o.getPosition().y > position.y){
            return 1;
        }else if(o.getPosition().y == position.y){
            return 0;
        }else{
            return -1;
        }

    }


}
