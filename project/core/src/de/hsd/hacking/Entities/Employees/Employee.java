package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.ColorHolder;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Tile.TileMovementProvider;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.*;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class Employee extends Entity implements Comparable<Employee>, Touchable {

    private ShapeRenderer debugRenderer;

    private final int BODY = 0;
    private final int HAIR = 1;
    private boolean touched;

    private boolean selected;

    /**
     * Returns the chance for the employee to have a critical failure
     * Critical failure is defined as a dice roll with a 20 sided dice that has a result lower than (1 + criticalFailureChance)
     * @return criticalFailureValue in the range 0-20. 0 represents no chance to have a critical failure, 20 means the employee always has a critical failure
     */
    public int getCriticalFailureChance() {
        //TODO abhängig von anderen Faktoren machen
        return 1;
    }
    /**
     * Returns the chance for the employee to have a critical success
     * Critical success is defined as a dice roll with a 20 sided dice that has a result greater than (20 - criticalSuccessChance)
     * @return criticalSuccessValue in the range 0-20. 0 represents no chance to have a critical success, 20 means the employee always has a critical success
     */
    public int getCriticalSuccessChance() {
        return 1;
    }

    public enum EmployeeSkillLevel {
        NOOB, INTERMEDIATE, PRO, WIZARD;

        private static final EmployeeSkillLevel[] VALUES = values();
        public static final int SIZE = VALUES.length;

        public static EmployeeSkillLevel getRandomSkillLevel() {
            return VALUES[MathUtils.random(SIZE - 1)];
        }
    }

    public enum HairStyle {
        CRAZY, NEAT, NERD, RASTA;
    }

    //Graphics
    private Assets assets;
    private Animation<TextureRegion>[][] animations;

    public enum AnimState {
        IDLE, WORKING, WORKING_BACKFACED, MOVING
    }

    private ShaderProgram shader;
    private AnimState animationState;
    private boolean flipped;

    //Data
    @Expose
    private String surName;
    @Expose
    private String lastName;
    @Expose
    private String description; // ? Needed ?
    @Expose
    private int salary;
    @Expose
    private EmployeeSkillLevel skillLevel;
    @Expose
    private ArrayList<Skill> skillSet;
    private float elapsedTime = MathUtils.random(3);
    private TileMovementProvider movementProvider;
    @Expose
    private HairStyle hairStyle;
    @Expose
    private Color hairColor, eyeColor, skinColor, shirtColor, trouserColor, shoeColor;

    public de.hsd.hacking.Entities.Employees.States.EmployeeState getState() {
        return state;
    }
    private Mission currentMission;

    @Expose
    private de.hsd.hacking.Entities.Employees.States.EmployeeState state;
    private Rectangle bounds;

    private int currentTileNumber;
    private int occupiedTileNumber;


    private GameStage stage;


    public Employee() {
        super(false, true, false);

        Init();
    }

    /**
     * Creates a new random employee
     *
     * @param level The desired skill Level
     */
    public Employee(EmployeeSkillLevel level) {
        super(false, true, false);

        //Create random name
        setName(DataLoader.getInstance().getNewName());
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
        salary = (300 + RandomUtils.randomInt(251)) * 100;
//                ls.random(300, 550) * 100;

        //RandomIntPool chooses a number randomly from a set of predefined numbers.
        //Used numbers can either be removed or left in the set.
        RandomIntPool pool = new RandomIntPool(new FromTo(0, skillSet.size() - 1));

        while (skillPoints > 0) {
            int randomInt = pool.getRandomNumberWithoutRemoving();
            Skill incrementSkill = skillSet.get(randomInt);
            if (incrementSkill.getValue() < 15) {
                incrementSkill.incrementSkill();
                skillPoints--;
            } else { //Skill at max -> Remove skill index from possible random indexes
                pool.removeNumber(randomInt);
            }
        }

        Init();
    }

    private void Init() {
        this.stage = GameStage.instance();
        this.assets = Assets.instance();
        movementProvider = stage.getTileMap();



        this.animationState = AnimState.IDLE;
        this.state = new de.hsd.hacking.Entities.Employees.States.IdleState(this);
        //Graphics
        setUpAnimations();
        setUpShader();

        debugRenderer = new ShapeRenderer();
    }

    /**
     * This is called as soon as the employee joins the team.
     */
    public void employ(){
        Tile startTile = movementProvider.getStartTile(this);
        Vector2 startPos = startTile.getPosition().cpy();
        this.currentTileNumber = this.occupiedTileNumber = startTile.getTileNumber();
        startTile.addEmployeeToDraw(this);
        this.bounds = new Rectangle(startPos.x + 5f, startPos.y + 5f, 22f, 45f); //values measured from sprite
        setPosition(startPos);
    }


    private void setUpShader() {
        String vertexShader = Shader.vertexShader;
        String fragmentShader = Shader.getFragmentShader(
                Color.valueOf(ColorHolder.HairColors.get(RandomUtils.randomInt(ColorHolder.HairColors.size()))),
                Color.valueOf(ColorHolder.SkinColors.get(RandomUtils.randomInt(ColorHolder.SkinColors.size()))),
                Color.valueOf(ColorHolder.ShirtColors.get(RandomUtils.randomInt(ColorHolder.ShirtColors.size()))),
                Color.valueOf(ColorHolder.TrouserColors.get(RandomUtils.randomInt(ColorHolder.TrouserColors.size()))),
                Color.valueOf(ColorHolder.EyesColors.get(RandomUtils.randomInt(ColorHolder.EyesColors.size()))),
                Color.valueOf(ColorHolder.ShoesColors.get(RandomUtils.randomInt(ColorHolder.ShoesColors.size()))));
        this.shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
        }
    }

    public void flipHorizontal(boolean toRight) {
        this.flipped = toRight;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (animationState == AnimState.WORKING){
            drawAt(batch, getPosition().sub(0, Constants.TILE_WIDTH / 4f), flipped, false, animationState);
        }else{
            drawAt(batch, getPosition(), flipped, false, animationState);
        }


        if (Constants.DEBUG) {
            batch.end();
            debugRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            debugRenderer.setTransformMatrix(batch.getTransformMatrix());
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(touched ? Color.GREEN : Color.RED);
            debugRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            debugRenderer.end();
            batch.begin();
        }
    }

    public void drawAt(Batch batch, Vector2 pos) {
        drawAt(batch, pos, false, true, AnimState.IDLE);
    }

    private void drawAt(Batch batch, Vector2 pos, boolean _flipped, boolean icon, AnimState _animationState) {
        batch.setShader(shader);
        if (selected && !icon) {
            batch.setColor(Color.RED);
        }
        Vector2 pixelPosition = clampToPixels(pos);
        for (int i = 0; i < 2; i++) {
            TextureRegion frame = animations[_animationState.ordinal()][i].getKeyFrame(elapsedTime, true);
            batch.draw(frame, _flipped ? pixelPosition.x + frame.getRegionWidth() : pixelPosition.x, pixelPosition.y, _flipped ? -frame.getRegionWidth() : frame.getRegionWidth(), frame.getRegionHeight());
        }
        batch.setShader(null);
        if (selected && !icon) {
            batch.setColor(Color.WHITE);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        animAct(delta);
        EmployeeState state = this.state.act(delta);
        if (state != null) {
            this.state.leave();
            this.state = state;
            this.state.enter();
        }
    }

    public void animAct(float delta){
        elapsedTime += delta;
    }


    public void removeFromDrawingTile() {
        movementProvider.getTile(currentTileNumber).removeEmployeeToDraw(this);
    }

    public void removeFromOccupyingTile() {
        movementProvider.getTile(occupiedTileNumber).setOccupyingEmployee(null);
    }

    @Override
    public String toString() {
        return "Employee: " + surName + " " + lastName + ". Skilllevel: " + this.skillLevel.name() + ". Skills: " + skillSetToString();
    }

    private Vector2 clampToPixels(Vector2 position) {
        return position.set((int) position.x, (int) position.y);
    }

    private String skillSetToString() {
        String skills = "";
        for (Skill skill :
                skillSet) {
            skills += skill.getType().name() + " : " + skill.getValue() + "; ";
        }
        return skills;
    }

    @Override
    public String getName() {
        return surName + " " + lastName;
    }

    public void resetElapsedTime() {
        this.elapsedTime = 0f;
    }

    private void setUpAnimations() {
        this.animations = new Animation[AnimState.values().length][2];
        int randHair = RandomUtils.randomInt(HairStyle.values().length);
        this.hairStyle = HairStyle.values()[randHair];
        Array<TextureRegion> hairframes = assets.getHairFrames(this.hairStyle);

        /* [1-3: Body Walkframes ]  */
        animations[AnimState.MOVING.ordinal()][BODY] = new Animation<TextureRegion>(.35f, assets.gray_character_body.get(0), assets.gray_character_body.get(1), assets.gray_character_body.get(2));
        animations[AnimState.MOVING.ordinal()][HAIR] = new Animation<TextureRegion>(.35f, hairframes.get(0), hairframes.get(1), hairframes.get(2));
        /* [1-2: Body Idleframes ]  */
        animations[AnimState.IDLE.ordinal()][BODY] = new Animation<TextureRegion>(.7f, assets.gray_character_body.get(2), assets.gray_character_body.get(2), assets.gray_character_body.get(2), assets.gray_character_body.get(3));
        animations[AnimState.IDLE.ordinal()][HAIR] = new Animation<TextureRegion>(.7f, hairframes.get(2), hairframes.get(2), hairframes.get(2), hairframes.get(3));
        /* [1: Body WorkingFrames  ] */
        animations[AnimState.WORKING.ordinal()][BODY] = new Animation<TextureRegion>(.5f, assets.gray_character_body.get(4));
        animations[AnimState.WORKING.ordinal()][HAIR] = new Animation<TextureRegion>(.5f, hairframes.get(4));

        animations[AnimState.WORKING_BACKFACED.ordinal()][BODY] = new Animation<TextureRegion>(.5f, assets.gray_character_body.get(6), assets.gray_character_body.get(7));
        animations[AnimState.WORKING_BACKFACED.ordinal()][HAIR] = new Animation<TextureRegion>(.5f, hairframes.get(6), hairframes.get(7));

    }

    @Override
    public boolean touchDown(Vector2 position) {
        if (bounds.contains(position)) {
            touched = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 position) {
        boolean t = false;
        if (touched && bounds.contains(position)) {
            onTouch();
            t = true;
        }
        touched = false;
        return t;
    }

    @Override
    public void touchDragged(Vector2 position) {
        //stub
    }

    @Override
    public int compareTo(Employee o) {
        if (o.getPosition().y > getPosition().y) {
            return 1;
        } else if (o.getPosition().y == getPosition().y && o.getPosition().x > getPosition().x) {
            return 1;
        } else if (o.getPosition().y == getPosition().y && o.getPosition().x == getPosition().x) {
            return 0;
        } else {
            return -1;
        }
    }

    private void onTouch() {
        toggleSelected();
    }

    public void toggleSelected() {
        if (!selected) stage.setSelectedEmployee(this);
        if (selected) stage.deselectEmployee();
        selected = !selected;
    }

    public boolean isSelected() {
        return selected;
    }

    //GETTER & SETTER

    public void setPosition(Vector2 position) {
        super.setPosition(position);

        this.bounds.setPosition(position.cpy().add(5f, 5f));
    }

    void setName(String first, String last) {
        this.surName = first;
        this.lastName = last;
    }

    void setName(String[] name) {
        this.surName = name[0];
        this.lastName = name[1];
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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public GameStage getStage() {
        return stage;
    }

//
//    public void setStage(GameStage stage) {
//        this.stage = stage;
//    }

    public Collection<Skill> getSkillset() {
        return Collections.unmodifiableCollection(skillSet);
    }

    public int getSkillValue(SkillType type) {
        int allPurpposeIndex = -1;
        for (int i = 0; i < skillSet.size(); i++) {
            Skill skill = skillSet.get(i);
            if (skill.getType() == type){
                return skill.getValue();
            }else if (skill.getType() == SkillType.All_Purpose){
                allPurpposeIndex = i;
            }
        }
        return skillSet.get(allPurpposeIndex).getValue();
    }

    public int getCurrentTileNumber() {
        return currentTileNumber;
    }

    public void setCurrentTileNumber(int currentTileNumber) {
        this.currentTileNumber = currentTileNumber;
    }

    public int getOccupiedTileNumber() {
        return occupiedTileNumber;
    }

    public void setOccupiedTileNumber(int occupiedTileNumber) {
        this.occupiedTileNumber = occupiedTileNumber;
    }

    public void setState(EmployeeState state) {
        this.state.cancel();
        this.state = state;
        this.state.enter();
    }

    public Mission getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(Mission currentMission) {
        this.currentMission = currentMission;
    }

    void setSkillSet(ArrayList<Skill> skillset){
        this.skillSet = skillset;
    }

    void setSalary(int salary){
        this.salary = salary;
    }

    public String getSalary(){ return String.format("%03d", salary) + "$";}

    public String getSalaryText() {
        return String.format("%03d", salary) + "$";
    }

    public int getHiringCost(){
        return (int) (salary * 1.5f /* * fraction of rest of week*/ );
    }

    public String getHiringCostText(){
        return String.format("%03d", getHiringCost()) + "$";
    }
}
