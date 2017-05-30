package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import com.google.gson.*;
import com.google.gson.annotations.*;

import java.util.ArrayList;
import java.util.Comparator;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.DataLoader;
import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Data.TileMovementProvider;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.FromTo;
import de.hsd.hacking.Utils.RandomIntPool;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class Employee extends Entity implements Comparable<Employee> {

    private final int SHADOW = 0;
    private final int LEGS = 1;
    private final int BODY = 2;
    private final int HEAD = 3;
    private final int HAIR = 4;

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

    // We need hair, eye, skin, shirt, trousers, shoes
    // hair can be replaced completely
    public String serialize() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(this);

        return json;
    }

    public void setAnimationState(AnimState animationState) {
        this.animationState = animationState;
    }
    public void flipHorizontal(boolean toRight){
        this.flipped = toRight;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < 5; i++) {
            TextureRegion frame = animations[animationState.ordinal()][i].getKeyFrame(elapsedTime, true);
            batch.draw(frame, flipped ? this.position.x + frame.getRegionWidth() : this.position.x, this.position.y, flipped ? -frame.getRegionWidth() : frame.getRegionWidth(), frame.getRegionHeight());
        }
        assets.gold_font_small.draw(batch, getName(), position.x - 30f, position.y + 70f, 92f, Align.center, false);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
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
        this.animations = new Animation[AnimState.values().length][5];

        animations[AnimState.MOVING.ordinal()][SHADOW] = new Animation(.5f, assets.default_character_shadow.get(0), assets.default_character_shadow.get(1));
        animations[AnimState.MOVING.ordinal()][LEGS] = new Animation(.5f, assets.default_character_legs.get(0), assets.default_character_legs.get(1));
        animations[AnimState.MOVING.ordinal()][BODY] = new Animation(.5f, assets.default_character_body.get(0), assets.default_character_body.get(1));
        animations[AnimState.MOVING.ordinal()][HEAD] = new Animation(.5f, assets.default_character_head.get(0), assets.default_character_head.get(1));
        animations[AnimState.MOVING.ordinal()][HAIR] = new Animation(.5f, assets.default_character_hair.get(0), assets.default_character_hair.get(1));

        animations[AnimState.IDLE.ordinal()][SHADOW] = new Animation(.5f, assets.default_character_shadow.get(2), assets.default_character_shadow.get(3));
        animations[AnimState.IDLE.ordinal()][LEGS] = new Animation(.5f, assets.default_character_legs.get(2), assets.default_character_legs.get(3));
        animations[AnimState.IDLE.ordinal()][BODY] = new Animation(.5f, assets.default_character_body.get(2), assets.default_character_body.get(3));
        animations[AnimState.IDLE.ordinal()][HEAD] = new Animation(.5f, assets.default_character_head.get(2), assets.default_character_head.get(3));
        animations[AnimState.IDLE.ordinal()][HAIR] = new Animation(.5f, assets.default_character_hair.get(2), assets.default_character_hair.get(3));

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
