package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.ColorHolder;
import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Data.Tile.TileMovementProvider;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.Risky;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Employees.States.IdleState;
import de.hsd.hacking.Entities.Employees.States.MovingState;
import de.hsd.hacking.Entities.Employees.States.WaitingState;
import de.hsd.hacking.Entities.Employees.States.WorkingState;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.*;

import static de.hsd.hacking.Entities.Employees.EmployeeFactory.SCORE_MISSION_COMPLETED;
import static de.hsd.hacking.Entities.Employees.EmployeeFactory.SCORE_MISSION_COMPLETED_PERLEVEL;
import static de.hsd.hacking.Entities.Employees.EmployeeFactory.SCORE_MISSION_CRITICAL_SUCCESS;

/**
 * An Employee is an Entity that ist created by the (@Link EmployeeFactory), hired and dismissed by the EmployeeManager and
 * drawn by the tile it stands on. It has an animated visual representation and may interact with a computer to work
 * on a mission. It may have different skills and specials.
 *
 * @author Florian, Hendrik, Julian
 */
public class Employee extends Entity implements Comparable<Employee>, Touchable, DataContainer {
    private Proto.Employee.Builder data;

    //Helper ints for getting animation information.
    private final int SHADOW = 0;
    private final int BODY = 1;

    private ShapeRenderer debugRenderer;

    private boolean touched;
    private boolean selected;

    //Graphics
    private Assets assets;
    private Animation<TextureRegion>[][] animations;

    private ShaderProgram colorShader;

    //Data
    private ArrayList<Skill> skillSet;
    private float elapsedTime = MathUtils.random(3);
    private TileMovementProvider movementProvider;

    public de.hsd.hacking.Entities.Employees.States.EmployeeState getState() {
        return state;
    }

    private Mission currentMission;

    private de.hsd.hacking.Entities.Employees.States.EmployeeState state;
    private Rectangle bounds;

    private ArrayList<EmployeeSpecial> employeeSpecials = new ArrayList<EmployeeSpecial>();
    private ArrayList<EmployeeSpecial> employeeSpecialsVisible = new ArrayList<EmployeeSpecial>();

    /**
     * Creates a default employee ready to be shaped by the EmployeeFactory.
     */
    public Employee() {
        super(false, true, false);
        assets = Assets.instance();
        data = Proto.Employee.newBuilder();
        randomVisualStyle();
        init(false);
    }

    public Employee(Proto.Employee.Builder data) {
        super(false, true, false);
        assets = Assets.instance();
        this.data = data;
        setUpAnimations();
        init(true);
    }

    /**
     * Initializes this employee.
     */
    private void init(boolean loaded) {
        assets = Assets.instance();

        data.setAnimState(Proto.Employee.AnimState.IDLE);
        state = new de.hsd.hacking.Entities.Employees.States.IdleState(this);
        //Graphics

        data.setAnimState(Proto.Employee.AnimState.IDLE);
        this.state = new de.hsd.hacking.Entities.Employees.States.IdleState(this);

        if (!loaded) {
            data.setCurrentTileNumber(-1);
            initColors();
        } else {
            skillSet = new ArrayList<Skill>();
            for (Proto.Skill.Builder builder : data.getSkillSetBuilderList()) {
                skillSet.add(new Skill(builder));
            }

            for (Proto.EmployeeSpecial proto : data.getEmployeeSpecialsList()) {
                try {
                    Class<?> spec = Class.forName(proto.getSpecial());
                    Constructor<?> constructor;
                    Object instance;

                    if (proto.getSpecial().equals("de.hsd.hacking.Entities.Employees.EmployeeSpecials.Risky")) {
                        constructor = spec.getConstructor(Employee.class, int.class);
                        instance = constructor.newInstance(this, proto.getLevel());
                    } else {
                        constructor = spec.getConstructor(Employee.class);
                        instance = constructor.newInstance(this);
                    }

                    addEmployeeSpecial((EmployeeSpecial) instance);
                } catch (Exception e) {
                    Gdx.app.error(Constants.TAG, e.toString());
                    Gdx.app.error(Constants.TAG, e.getStackTrace().toString());
                }
            }
        }

        setUpShader();
        debugRenderer = new ShapeRenderer();
    }

    /**
     * This is called by the {@link EmployeeFactory} when the creation process is finished.
     */
    void finishCreation() {
        setUpAnimations();
    }

    /**
     * This is called as soon as the employee is hired.
     */
    public void onEmploy() {
        setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

        data.setIsEmployed(true);

        if (GameStage.instance() != null) initEmployeePosition();

        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onEmploy();
        }
    }

    /**
     * This is called when this employee is deserialized.
     */
    public void onLoad() {
        if (!data.getIsEmployed()) return;
    }

    /**
     * Sets the starting position of this employee.
     *
     * @param startTile
     */
    private void setStartingTile(Tile startTile) {
        Vector2 startPos = startTile.getPosition().cpy();
        startTile.addEmployeeToDraw(this);
        startTile.setOccupyingEmployee(this);
        this.bounds = new Rectangle(startPos.x + 5f, startPos.y + 5f, 22f, 45f); //values measured from sprite
        setPosition(startPos);
    }

    /**
     *
     */
    public void initEmployeePosition() {
        movementProvider = GameStage.instance().getTileMap();
        int tileNr = data.getCurrentTileNumber();

        if (tileNr == -1) setStartingTile(GameStage.instance().getTileMap().findAndSetStartTile(this));
        else setStartingTile(GameStage.instance().getTileMap().getTile(tileNr));
    }

    /**
     *
     */
    public void restoreWorkingState() {
        if (MissionManager.instance().getActiveMissions().size() > 0)
            if (data.getMissionNumber() != -1) {
                currentMission = MissionManager.instance().getActiveMission(data.getMissionNumber());
            }

        switch (data.getState().getState()) {

            case IDLE:
            case MOVING:
            case WAITING:
            case UNRECOGNIZED:
                data.setAnimState(Proto.Employee.AnimState.IDLE);
                this.state = new de.hsd.hacking.Entities.Employees.States.IdleState(this);
                break;
            case WORKING:
//                data.setAnimState(Proto.Employee.AnimState.WORKING);
//                Vector2 vector = new Vector2(data.getState().getWorkingX(), data.getState().getWorkingY());
                Computer computer;
                for (Equipment equipment: EquipmentManager.instance().getPurchasedItemList()) {
                    if (equipment.getClass() != Computer.class)
                        continue;

                    if (equipment.getName().contains(Integer.toString(data.getState().getComputer()))) {
                        computer = (Computer) equipment;

                        setState(new MovingState(this, movementProvider.getDiscreteTile(computer.getWorkingChair().getPosition())));
                    }
                }

                break;
        }
    }

    /**
     * This is called as soon as the employee leaves the teamManager.
     */
    void onDismiss() {
        state.cancel();
        data.setIsEmployed(false);
        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onDismiss();
        }
    }

    /**
     * This is called as soon as the employee levels up.
     */
    void onLevelUp() {
        EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.LEVELUP, this);

        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onLevelUp();
        }

        //TODO: Gehaltsverhandlungen starten

    }

    void getLevel() {

    }

    private void initColors() {
        data.setHairColor(ColorHolder.HairColors.get(RandomUtils.randomInt(ColorHolder.HairColors.size())));
        data.setSkinColor(ColorHolder.SkinColors.get(RandomUtils.randomInt(ColorHolder.SkinColors.size())));
        data.setShirtColor(ColorHolder.ShirtColors.get(RandomUtils.randomInt(ColorHolder.ShirtColors.size())));
        data.setTrouserColor(ColorHolder.TrouserColors.get(RandomUtils.randomInt(ColorHolder.TrouserColors.size())));
        data.setEyeColor(ColorHolder.EyesColors.get(RandomUtils.randomInt(ColorHolder.EyesColors.size())));
        data.setShoeColor(ColorHolder.ShoesColors.get(RandomUtils.randomInt(ColorHolder.ShoesColors.size())));
    }

    /**
     * Initializes the colorShader for this employee.
     */
    private void setUpShader() {

        this.colorShader = new ShaderProgram(Shader.VERTEX_SHADER, Shader.getEmployeeFragmentShader(
                Color.valueOf(data.getHairColor()),
                Color.valueOf(data.getSkinColor()),
                Color.valueOf(data.getShirtColor()),
                Color.valueOf(data.getTrouserColor()),
                Color.valueOf(data.getEyeColor()),
                Color.valueOf(data.getShoeColor()))
        );

        if (!colorShader.isCompiled()) {
            throw new GdxRuntimeException("Couldn't compile colorShader: " + colorShader.getLog());
        }
    }

    /**
     * Flips the employee sprite according to view direction.
     *
     * @param toRight
     */
    public void flipHorizontal(boolean toRight) {
        data.setFlipped(toRight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (data.getAnimState() == Proto.Employee.AnimState.WORKING) {
            drawAt(batch, parentAlpha, getPosition().sub(0, Constants.TILE_WIDTH / 4f), data.getFlipped(), false, data.getAnimState());
        } else {
            drawAt(batch, parentAlpha, getPosition(), data.getFlipped(), false, data.getAnimState());
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

    /**
     * Draws this employee at a desired position on the screen. Used to draw employee icons in UI.
     *
     * @param batch
     * @param pos
     */
    public void drawAt(Batch batch, Vector2 pos) {
        drawAt(batch, 1f, pos, false, true, Proto.Employee.AnimState.IDLE);
    }

    private void drawAt(Batch batch, float parentAlpha, Vector2 pos, boolean _flipped, boolean icon, Proto.Employee.AnimState _animationState) {

        Vector2 pixelPosition = clampToPixels(pos);
        TextureRegion frame = animations[_animationState.ordinal()][SHADOW].getKeyFrame(elapsedTime, true);
        batch.draw(frame, _flipped ? pixelPosition.x + frame.getRegionWidth() : pixelPosition.x, pixelPosition.y, _flipped ? -frame.getRegionWidth() : frame.getRegionWidth(), frame.getRegionHeight());
        batch.end();
        colorShader.begin();

        TextureRegion charFrame = animations[_animationState.ordinal()][BODY].getKeyFrame(elapsedTime, true);
        if (selected && !icon) {
            colorShader.setUniformi("sel", 1);
            colorShader.setUniformf("u_viewportInverse", new Vector2(1f / charFrame.getTexture().getWidth(), 1f / charFrame.getTexture().getHeight()));
        } else {
            colorShader.setUniformi("sel", 0);
        }
        colorShader.end();

        batch.begin();
        batch.setShader(colorShader);

        batch.draw(charFrame, _flipped ? pixelPosition.x + charFrame.getRegionWidth() : pixelPosition.x, pixelPosition.y, _flipped ? -charFrame.getRegionWidth() : charFrame.getRegionWidth(), charFrame.getRegionHeight());

        batch.setShader(null);

        for (EmployeeSpecial special : employeeSpecials) {
            special.draw(batch, parentAlpha);
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
        for (EmployeeSpecial special : employeeSpecials) {
            special.act(delta);
        }
        this.bounds.setPosition(getPosition().cpy().add(5f, 5f));
    }

    public void animAct(float delta) {
        elapsedTime += delta;
    }

    public void removeFromDrawingTile() {
        if (data.getCurrentTileNumber() > -1)
            movementProvider.getTile(data.getCurrentTileNumber()).removeEmployeeToDraw(this);
    }

    public void removeFromOccupyingTile() {
        if (data.getOccupiedTileNumber() > -1)
            movementProvider.getTile(data.getOccupiedTileNumber()).setOccupyingEmployee(null);
    }

    @Override
    public String toString() {
        return "Employee: " + getName() + " Used score:" + getUsedScore() + " Free score:" + getFreeScore() + " Skills: " + skillSetToString() + " Salary:" + getSalaryText();
    }

    private Vector2 clampToPixels(Vector2 position) {
        return position.set((int) position.x, (int) position.y);
    }

    private String skillSetToString() {
        String skills = "";
        for (Skill skill :
                skillSet) {
            skills += skill.getType().skillType.name() + " : " + skill.getValue() + "; ";
        }
        return skills;
    }

    /**
     * Returns this employees full name.
     *
     * @return
     */
    @Override
    public String getName() {
        return data.getName() + " " + data.getSurName();
    }

    public void resetElapsedTime() {
        this.elapsedTime = 0f;
    }

    /**
     * Randomizes Hairstyles.
     */
    private void randomVisualStyle() {
        data.setVisualStyle(Proto.Employee.VisualStyle.DEFAULT);
        data.setHairStyleFemaleValue(RandomUtils.randomInt(Proto.Employee.HairStyleFemale.values().length - 1));
        data.setHairStyleMaleValue(RandomUtils.randomInt(Proto.Employee.HairStyleMale.values().length - 1));
    }

    /**
     * Defines all animations for this employee based on visual settings.
     */
    private void setUpAnimations() {
        this.animations = new Animation[Proto.Employee.AnimState.values().length][2];

        Array<TextureRegion> bodyFrames = assets.getCharBody(data.getVisualStyle(), data.getGender(), data.getHairStyleFemale(), data.getHairStyleMale());
        Array<TextureRegion> shadowFrames = assets.getCharShadow(data.getVisualStyle());


        switch (data.getVisualStyle()) {
            case TRUMP:
                /* [1-2: Body Walkframes ]  */
                animations[Proto.Employee.AnimState.MOVING.ordinal()][SHADOW] = Assets.getFrames(.2f, shadowFrames, 0, 0);
                animations[Proto.Employee.AnimState.MOVING.ordinal()][BODY] = Assets.getFrames(.2f, bodyFrames, 0, 3);

                /* [1-2: Body Idleframes ]  */
                animations[Proto.Employee.AnimState.IDLE.ordinal()][SHADOW] = Assets.getFrames(.2f, shadowFrames, 0, 0);
                animations[Proto.Employee.AnimState.IDLE.ordinal()][BODY] = Assets.getFrames(.2f, bodyFrames, 4, 15);

                /* [1: Body WorkingFrames  ] */
                animations[Proto.Employee.AnimState.WORKING.ordinal()][SHADOW] = Assets.getFrames(.2f, shadowFrames, 0, 0);
                animations[Proto.Employee.AnimState.WORKING.ordinal()][BODY] = Assets.getFrames(.2f, bodyFrames, 16, 30);

                animations[Proto.Employee.AnimState.WORKING_BACKFACED.ordinal()][SHADOW] = Assets.getFrames(.2f, shadowFrames, 0, 0);
                animations[Proto.Employee.AnimState.WORKING_BACKFACED.ordinal()][BODY] = Assets.getFrames(.2f, bodyFrames, 16, 30);
                break;

            default:
                /* [1-2: Body Walkframes ]  */
                animations[Proto.Employee.AnimState.MOVING.ordinal()][SHADOW] = Assets.getFrames(.35f, shadowFrames, 0, 2);
                animations[Proto.Employee.AnimState.MOVING.ordinal()][BODY] = Assets.getFrames(.35f, bodyFrames, 0, 2);

                /* [1-2: Body Idleframes ]  */
                animations[Proto.Employee.AnimState.IDLE.ordinal()][SHADOW] = new Animation<TextureRegion>(.7f, shadowFrames.get(2), shadowFrames.get(2), shadowFrames.get(2), shadowFrames.get(3));
                animations[Proto.Employee.AnimState.IDLE.ordinal()][BODY] = new Animation<TextureRegion>(.7f, bodyFrames.get(2), bodyFrames.get(2), bodyFrames.get(2), bodyFrames.get(3));

                /* [1: Body WorkingFrames  ] */
                animations[Proto.Employee.AnimState.WORKING.ordinal()][SHADOW] = Assets.getFrames(.5f, shadowFrames, 4, 4);
                animations[Proto.Employee.AnimState.WORKING.ordinal()][BODY] = Assets.getFrames(.5f, bodyFrames, 4, 4);

                animations[Proto.Employee.AnimState.WORKING_BACKFACED.ordinal()][SHADOW] = Assets.getFrames(.5f, shadowFrames, 6, 7);
                animations[Proto.Employee.AnimState.WORKING_BACKFACED.ordinal()][BODY] = Assets.getFrames(.5f, bodyFrames, 6, 7);
                break;
        }
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
        Gdx.app.log(Constants.TAG, "Employee onTouch!");
        toggleSelected();
        for (EmployeeSpecial special : employeeSpecials) {
            special.onTouch();
        }
    }

    private void toggleSelected() {
        if (!selected) {
            select();
        } else {
            deselect();
        }
    }

    private void select() {
        if (TeamManager.instance().isEmployeeSelected()) {
            TeamManager.instance().deselectEmployee();
        }
        this.selected = true;
        TeamManager.instance().setSelectedEmployee(this);
        MissionWorker missionWorker = MissionManager.instance().getMissionWorker(this);
        if (missionWorker != null) {
            GameStage.instance().showMissionStatusOverlay(missionWorker, this);
        }
    }

    public void deselect() {
        this.selected = false;
        TeamManager.instance().deselectEmployee();
        GameStage.instance().hideMissionStatusOverlay();
    }

    public void setSelected(final boolean newSelected) {
        if (!(newSelected == this.selected)) {
            if (newSelected) {
                select();
            } else {
                deselect();
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setPosition(Vector2 position) {
        super.setPosition(position);

        this.bounds.setPosition(position.cpy().add(5f, 5f));
    }

    void setName(String first, String last) {
        data.setName(first);
        data.setSurName(last);
    }

    void setName(String[] name) {
        data.setName(name[0]);
        data.setSurName(name[1]);
    }

    public TileMovementProvider getMovementProvider() {
        return movementProvider;
    }

    public Proto.Employee.AnimState getAnimationState() {
        return data.getAnimState();
    }

    public void setAnimationState(Proto.Employee.AnimState animationState) {
        data.setAnimState(animationState);
    }

    // region Skills

    void learnSkill(Skill skill, boolean sort) {
        skillSet.add(skill);
        if (sort) Collections.sort(skillSet);
    }

    /**
     * Returns true if this employee has the skill of the given type.
     *
     * @param type
     * @return
     */
    public boolean hasSkill(Proto.SkillType type) {
        for (Skill skill : skillSet) {
            if (skill.getType().skillType == type) return true;
        }

        return false;
    }

    void sortSkills() {
        Collections.sort(skillSet);
    }



    void setSkillSet(ArrayList<Skill> skillset) {
//        this.skillSet = new ArrayList<Skill>(skillset);
        this.skillSet = skillset;
        Collections.sort(skillSet);
    }

    /**
     * Returns the skills of this employee.
     * @return
     */
    public Collection<Skill> getSkillset() {
        return Collections.unmodifiableCollection(skillSet);
    }

    /**
     * Returns the skill value of the given skill or the Allpurpose skill level of the employee doesnt have the skill.
     *
     * @param type
     * @return
     */
    public int getSkillValue(SkillType type) {
        int allPurpposeIndex = -1;
        for (int i = 0; i < skillSet.size(); i++) {
            Skill skill = skillSet.get(i);
            if (skill.getType() == type) {
                return evaluateSkill(skill);
            } else if (skill.getType().skillType == Proto.SkillType.All_Purpose) {
                allPurpposeIndex = i;
            }
        }
        return evaluateSkill(skillSet.get(allPurpposeIndex));
    }

    public boolean hasSkill(SkillType type){
        for (Skill skill : skillSet) {
            if (skill.getType() == type) return true;
        }

        return false;
    }

    /**
     * Returns the skill level of the given skill using special and equipment bonuses.
     *
     * @param skill
     * @return
     */
    private int evaluateSkill(Skill skill) {

        int specialAbsoluteBonus = 0;
        float specialRelativeBonus = 1;

        for (EmployeeSpecial s : employeeSpecials) {
            specialAbsoluteBonus += s.getSkillAbsoluteBonus(skill.getType());
            specialRelativeBonus *= s.getSkillRelativeFactor(skill.getType());
        }

        specialAbsoluteBonus += TeamManager.instance().resources.getSkillBonus(skill);

        return (int) ((skill.getValue() + specialAbsoluteBonus) * specialRelativeBonus);
    }

    // endregion

    public int getCurrentTileNumber() {
        return data.getCurrentTileNumber();
    }

    public void setCurrentTileNumber(int currentTileNumber) {
        data.setCurrentTileNumber(currentTileNumber);
    }

    public int getOccupiedTileNumber() {
        return data.getOccupiedTileNumber();
    }

    public void setOccupiedTileNumber(int occupiedTileNumber) {
        data.setOccupiedTileNumber(occupiedTileNumber);
    }

    /**
     * Makes this employee enter the given state.
     *
     * @param state
     */
    public void setState(EmployeeState state) {
        this.state = state;
        this.state.enter();
    }

    public Mission getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(Mission currentMission) {
        this.currentMission = currentMission;
        data.setMissionNumber(MissionManager.instance().getActiveMissionId(currentMission));
    }

    void setGender(Proto.Employee.Gender gender) {
        data.setGender(gender);
    }

    public Proto.Employee.Gender getGender() {
        return data.getGender();
    }

    void setVisualStyle(Proto.Employee.VisualStyle visualStyle) {
        data.setVisualStyle(visualStyle);
    }

    // region Salary

    void setSalary(int salary) {
        data.setSalary(salary);
    }

    /**
     * Calculates and returns the salary of this employee.
     *
     * @return
     */
    int getSalary() {

        int specialAbsoluteBonus = 0;
        float specialRelativeBonus = 1;

        for (EmployeeSpecial s : employeeSpecials) {
            specialAbsoluteBonus += s.getSalaryAbsoluteBonus();
            specialRelativeBonus *= s.getSalaryRelativeFactor();
        }

        return (int) (data.getSalary() * specialRelativeBonus) + specialAbsoluteBonus;
    }

    /**
     * Returns the salary of this employee in readable string format.
     *
     * @return
     */
    public String getSalaryText() {
        return String.format("%03d", data.getSalary()) + "$";
    }

    /**
     * Calculates and returns the hiring cost of this employee.
     *
     * @return
     */
    int getHiringCost() {

        int specialAbsoluteBonus = 0;
        float specialRelativeBonus = 1;

        for (EmployeeSpecial s : employeeSpecials) {
            specialAbsoluteBonus += s.getHiringCostAbsoluteBonus();
            specialRelativeBonus *= s.getHiringCostRelativeFactor();
        }

        return (int) (data.getSalary() * 1.5f * GameTime.instance().getRemainingWeekFraction() * specialRelativeBonus) + specialAbsoluteBonus;
    }

    public String getHiringCostText() {
        return String.format("%03d", getHiringCost()) + "$";
    }

    // endregion

    // region Employee Specials

    /**
     * Adds a new employee special in case the employee does not already have this kind of special and he meets all specials requirements.
     *
     * @param special
     * @return Returns the balancing score value of this special in case it is added. 0 otherwise.
     */
    float addEmployeeSpecial(EmployeeSpecial special) {

        if (!special.isApplicable()) return 0; //this special cannot be learned by this employee

        if (data.getIsEmployed() && !special.isLearnable())
            return 0; //this special cannot be added to an employed employee

        for (EmployeeSpecial s : employeeSpecials) {
            if (s.getClass() == special.getClass()) return 0; //employee already has this special
        }

        if (data.getIsEmployed()) {
            if (!special.isHidden())
                MessageManager.instance().Info(getName() + " gained the '" + special.getDisplayName() + "' ability.");
        }

        employeeSpecials.add(special);
        if (!special.isHidden()) employeeSpecialsVisible.add(special);
        return special.getScoreCost();
    }

    /**
     * Returns a readonly List of all employee specials.
     *
     * @return
     */
    public Collection<EmployeeSpecial> getSpecials(boolean includeHidden) {
        if (includeHidden) return Collections.unmodifiableCollection(employeeSpecials);
        else return Collections.unmodifiableCollection(employeeSpecialsVisible);
    }

    // endregion

    public boolean isEmployed() {
        return data.getIsEmployed();
    }

    /**
     * Returns the chance for the employee to have a critical failure
     * Critical failure is defined as a dice roll with a 20 sided dice that has a result lower than (1 + criticalFailureChance)
     *
     * @return criticalFailureValue in the range 0-20. 0 represents no chance to have a critical failure, 20 means the employee always has a critical failure
     */
    public int getCriticalFailureChance() {

        int result = 0;

        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            result += special.getCriticalFailureBonus();
        }

        return 1 + result;
    }

    /**
     * Returns the chance for the employee to have a critical emoji_success
     * Critical emoji_success is defined as a dice roll with a 20 sided dice that has a result greater than (20 - criticalSuccessChance)
     *
     * @return criticalSuccessValue in the range 0-20. 0 represents no chance to have a critical emoji_success, 20 means the employee always has a critical emoji_success
     */
    public int getCriticalSuccessChance() {

        int result = 0;

        for (EmployeeSpecial special : employeeSpecials) {
            result += special.getCriticalSuccessBonus();
        }

        return 1 + result;
    }

//    void incrementUsedScore(float score) {
//        usedScore += score;
//    }

    /**
     * Adds score points for
     *
     * @param score
     */
    public void incrementFreeScore(float score) {
        data.setFreeScore(data.getFreeScore() + score);
        //Gdx.app.log(Constants.TAG, getName() + " gets " + score + " score points.");
    }

    /**
     * Spends score points for this employee.
     *
     * @param score
     */
    void useScore(float score) {
        data.setUsedScore(data.getUsedScore() + score);
        data.setFreeScore(data.getFreeScore() - score);
    }

    public float getUsedScore() {
        return data.getUsedScore();
    }

    public float getFreeScore() {
        return data.getFreeScore();
    }

    /**
     * This is called
     */
    public void onMissionCompleted() {

        for (EmployeeSpecial special : employeeSpecials) {
            special.onMissionCompleted();
        }

        incrementFreeScore(SCORE_MISSION_COMPLETED + currentMission.getDifficulty() * SCORE_MISSION_COMPLETED_PERLEVEL);

        EmployeeFactory.levelUp(this);
    }

    public void onMissionCriticalSuccess() {

        incrementFreeScore(SCORE_MISSION_CRITICAL_SUCCESS);

        EmployeeFactory.levelUp(this);
    }

    public static Proto.Employee.Gender randomGender() {
        if (RandomUtils.randomIntWithin(0, 1) == 0) return Proto.Employee.Gender.MALE;
        else return Proto.Employee.Gender.FEMALE;
    }

    public Proto.Employee getData() {
        data.clearSkillSet();

        for (Skill skill : skillSet) {
            data.addSkillSet(skill.getData());
        }

        for (EmployeeSpecial specialus : employeeSpecials) {
            Proto.EmployeeSpecial.Builder builder = Proto.EmployeeSpecial.newBuilder();
            builder.setSpecial(specialus.getClass().getName());

            if (specialus.getClass() == Risky.class)
                builder.setLevel(((Risky) specialus).getLevel());

            data.addEmployeeSpecials(builder.build());
        }

        Proto.EmployeeState.Builder stateBuilder = Proto.EmployeeState.newBuilder();

        if (state.getClass() == IdleState.class) {
            stateBuilder.setState(Proto.EmployeeState.State.IDLE);
        }
        else if (state.getClass() == MovingState.class) {
            stateBuilder.setState(Proto.EmployeeState.State.IDLE);
        }
        else if (state.getClass() == WaitingState.class) {
            stateBuilder.setState(Proto.EmployeeState.State.IDLE);
        }
        else if (state.getClass() == WorkingState.class) {
            stateBuilder.setState(Proto.EmployeeState.State.WORKING);
            stateBuilder.setWorkingX(((WorkingState)state).getWorkingPosition().x);
            stateBuilder.setWorkingY(((WorkingState)state).getWorkingPosition().y);
            Computer computer = ((WorkingState) state).getComputer();
            int number = Integer.parseInt(computer.getName().substring(computer.getName().length() - 1));
            stateBuilder.setComputer(number);
        }

        data.setState(stateBuilder.build());

        return data.build();
    }
}
