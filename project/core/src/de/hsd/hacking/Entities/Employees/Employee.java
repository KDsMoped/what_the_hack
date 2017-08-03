package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.ColorHolder;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Data.Tile.TileMovementProvider;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.Risky;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Entities.Team.Team;
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
public class Employee extends Entity implements Comparable<Employee>, Touchable {
    private Proto.Employee.Builder data;

    private final int BODY = 0;
    private final int HAIR = 1;

    private ShapeRenderer debugRenderer;

    private boolean touched;
    private boolean selected;

//    public enum EmployeeSkillLevel {
//        NOOB, INTERMEDIATE, PRO, WIZARD;
//
//        private static final EmployeeSkillLevel[] VALUES = values();
//        public static final int SIZE = VALUES.length;
//
//        public static EmployeeSkillLevel getRandomSkillLevel() {
//            return VALUES[MathUtils.random(SIZE - 1)];
//        }
//    }

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

    private transient GameStage stage;

    /**
     * Creates a default employee ready to be shaped by the EmployeeFactory.
     */
    public Employee() {
        super(false, true, false);
        data = Proto.Employee.newBuilder();
        init(false);
    }

    public Employee(Proto.Employee.Builder data) {
        super(false, true, false);
        this.data = data;
        init(true);
    }

//    /**
//     * Creates a new random employee. This is @deprecated and replaced by {@link Employee()}.
//     *
//     * @param level The desired skill Level
//     */
//    @Deprecated
//    public Employee(EmployeeSkillLevel level) {
//        super(false, true, false);
//
//        //Create random name
//        setName(DataLoader.getInstance().getNewName(Gender.UNDECIDED));
//        this.skillLevel = level;
//
//        //Skill points to spend. NOOB = 55, INTERMEDIATE = 65, PRO = 75, WIZARD = 85
//        //35 Points are spent by default (5 per Skill)
//        int skillPoints = 55 + skillLevel.ordinal() * 10;
//        skillSet = new ArrayList<Skill>(7);
//        for (SkillType type :
//                SkillType.values()) {
//            skillSet.add(new Skill(type, 5));
//            skillPoints -= 5;
//        }
//        salary = (300 + RandomUtils.randomInt(251)) * 100;
////                ls.random(300, 550) * 100;
//
//        //RandomIntPool chooses a number randomly from a set of predefined numbers.
//        //Used numbers can either be removed or left in the set.
//        RandomIntPool pool = new RandomIntPool(new FromTo(0, skillSet.size() - 1));
//
//        while (skillPoints > 0) {
//            int randomInt = pool.getRandomNumberWithoutRemoving();
//            Skill incrementSkill = skillSet.get(randomInt);
//            if (incrementSkill.getValue() < 15) {
//                incrementSkill.incrementSkill();
//                skillPoints--;
//            } else { //Skill at max -> Remove skill index from possible random indexes
//                pool.removeNumber(randomInt);
//            }
//        }
//        init();
//    }

    /**
     * Initializes this employee.
     */
    private void init(boolean loaded) {
        this.stage = GameStage.instance();
        this.assets = Assets.instance();
        movementProvider = stage.getTileMap();

        this.animationState = AnimState.IDLE;
        this.state = new de.hsd.hacking.Entities.Employees.States.IdleState(this);
        //Graphics
        setUpAnimations();

        if (!loaded) {
            initColors();
        }
        else {
            skillSet = new ArrayList<Skill>();
            for (Proto.Skill.Builder builder: data.getSkillSetBuilderList()) {
                skillSet.add(new Skill(builder));
            }

            for (Proto.EmployeeSpecial proto: data.getEmployeeSpecialsList()) {
                try {
                    Class<?> spec = Class.forName(proto.getSpecial());
                    Constructor<?> constructor;
                    Object instance;

                    if (proto.getSpecial().equals("de.hsd.hacking.Entities.Employees.EmployeeSpecials.Risky")) {
                        constructor = spec.getConstructor(Employee.class, int.class);
                        instance = constructor.newInstance(this, proto.getLevel());
                    }
                    else {
                        constructor = spec.getConstructor(Employee.class);
                        instance = constructor.newInstance(this);
                    }

                    addEmployeeSpecial((EmployeeSpecial)instance);
                }
                catch (Exception e) {
                    Gdx.app.error(Constants.TAG, e.toString());
                    Gdx.app.error(Constants.TAG, e.getStackTrace().toString());
                }
            }
        }

        setUpShader();
        debugRenderer = new ShapeRenderer();
    }

    /**
     * This is called as soon as the employee joins the team.
     */
    public void onEmploy() {
        setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        Tile startTile = movementProvider.getStartTile(this);
        Vector2 startPos = startTile.getPosition().cpy();
//        this.currentTileNumber = this.occupiedTileNumber = startTile.getTileNumber();
        startTile.addEmployeeToDraw(this);
        startTile.setOccupyingEmployee(this);
        this.bounds = new Rectangle(startPos.x + 5f, startPos.y + 5f, 22f, 45f); //values measured from sprite
        setPosition(startPos);
        data.setIsEmployed(true);

        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onEmploy();
        }
    }

    /**
     * This is called as soon as the employee leaves the team.
     */
    public void onDismiss() {
        state.cancel();
        data.setIsEmployed(false);
        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onDismiss();
        }
    }

    /**
     * This is called as soon as the employee levels up.
     */
    public void onLevelUp() {
        EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.LEVELUP, this);

        for (EmployeeSpecial special : employeeSpecials.toArray(new EmployeeSpecial[employeeSpecials.size()])) {
            special.onLevelUp();
        }

        //TODO: Gehaltsverhandlungen starten

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
     * Initializes the shader for this employee.
     */
    private void setUpShader() {
        String vertexShader = Shader.VERTEX_SHADER;
        String fragmentShader = Shader.getEmployeeFragmentShader(
                Color.valueOf(data.getHairColor()),
                Color.valueOf(data.getSkinColor()),
                Color.valueOf(data.getShirtColor()),
                Color.valueOf(data.getTrouserColor()),
                Color.valueOf(data.getEyeColor()),
                Color.valueOf(data.getShoeColor()));
        this.shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
        }
    }

    /**
     * Flipps the employee sprite according to view direction.
     * @param toRight
     */
    public void flipHorizontal(boolean toRight) {
        this.flipped = toRight;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (animationState == AnimState.WORKING) {
            drawAt(batch, parentAlpha, getPosition().sub(0, Constants.TILE_WIDTH / 4f), flipped, false, animationState);
        } else {
            drawAt(batch, parentAlpha, getPosition(), flipped, false, animationState);
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
     * @param batch
     * @param pos
     */
    public void drawAt(Batch batch, Vector2 pos) {
        drawAt(batch, 1f, pos, false, true, AnimState.IDLE);
    }

    private void drawAt(Batch batch, float parentAlpha, Vector2 pos, boolean _flipped, boolean icon, AnimState _animationState) {

        batch.end();
        shader.begin();

        if (selected && !icon) {
            shader.setUniformi("sel", 1);
            shader.setUniformf("u_viewportInverse", new Vector2(1f / Constants.VIEWPORT_WIDTH, 1f / Constants.VIEWPORT_HEIGHT));
        } else {
            shader.setUniformi("sel", 0);
        }
        shader.end();
        batch.setShader(shader);
        batch.begin();
        Vector2 pixelPosition = clampToPixels(pos);
        for (int i = 0; i < 2; i++) {
            TextureRegion frame = animations[_animationState.ordinal()][i].getKeyFrame(elapsedTime, true);
            batch.draw(frame, _flipped ? pixelPosition.x + frame.getRegionWidth() : pixelPosition.x, pixelPosition.y, _flipped ? -frame.getRegionWidth() : frame.getRegionWidth(), frame.getRegionHeight());
        }
        batch.setShader(null);
        if (selected && !icon) {
//            batch.setColor(Color.WHITE);
        }

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
        movementProvider.getTile(data.getCurrentTileNumber()).removeEmployeeToDraw(this);
    }

    public void removeFromOccupyingTile() {
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
     * @return
     */
    @Override
    public String getName() {
        return data.getName() + " " + data.getSurName();
    }

    public void resetElapsedTime() {
        this.elapsedTime = 0f;
    }

    private void setUpAnimations() {
        this.animations = new Animation[AnimState.values().length][2];
        int randHair = RandomUtils.randomInt(Proto.Employee.HairStyle.values().length - 1);
        data.setHairStyle(Proto.Employee.HairStyle.values()[randHair]);
        Array<TextureRegion> hairframes = assets.getHairFrames(data.getHairStyle());

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
        if (Team.instance().isEmployeeSelected()) {
            Team.instance().deselectEmployee();
        }
        this.selected = true;
        Team.instance().setSelectedEmployee(this);
        MissionWorker missionWorker = MissionManager.instance().getMissionWorker(this);
        if (missionWorker != null) {
            GameStage.instance().showMissionStatusOverlay(missionWorker, this);
        }
    }

    public void deselect() {
        this.selected = false;
        Team.instance().deselectEmployee();
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

    //GETTER & SETTER

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

    public AnimState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimState animationState) {
        this.animationState = animationState;
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
            } else if (skill.getType().skillType == Proto.Skill.SkillType.All_Purpose) {
                allPurpposeIndex = i;
            }
        }
        return evaluateSkill(skillSet.get(allPurpposeIndex));
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

        specialAbsoluteBonus += Team.instance().resources.getSkillBonus(skill);

        return (int) ((skill.getValue() + specialAbsoluteBonus) * specialRelativeBonus);
    }

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
    }

    void setGender(Proto.Employee.Gender gender) {
        data.setGender(gender);
    }

    public Proto.Employee.Gender getGender() {
        return data.getGender();
    }

    void setSkillSet(ArrayList<Skill> skillset) {
//        this.skillSet = new ArrayList<Skill>(skillset);
        this.skillSet = skillset;
        Collections.sort(skillSet);
    }

    void learnSkill(Skill skill, boolean sort) {
        skillSet.add(skill);
        if (sort) Collections.sort(skillSet);
    }

    void sortSkills() {
        Collections.sort(skillSet);
    }

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

        return (int) (data.getSalary() * 1.5f * GameTime.instance.getRemainingWeekFraction() * specialRelativeBonus) + specialAbsoluteBonus;
    }

    public String getHiringCostText() {
        return String.format("%03d", getHiringCost()) + "$";
    }

    /**
     * Adds a new employee special in case the employee does not already have this kind of special and he meets all specials requirements.
     *
     * @param special
     * @return Returns the balancing score value of this special in case it is added. 0 otherwise.
     */
    float addEmployeeSpecial(EmployeeSpecial special) {

        if (!special.isApplicable()) return 0; //this special cannot be learned by this employee

        if (data.getIsEmployed() && !special.isLearnable()) return 0; //this special cannot be added to an employed employee

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

    public boolean isEmployed() {
        return data.getIsEmployed();
    }

    /**
     * Returns true if this employee has the skill of the given type.
     *
     * @param type
     * @return
     */
    public boolean hasSkill(Proto.Skill.SkillType type) {
        for (Skill skill : skillSet) {
            if (skill.getType().skillType == type) return true;
        }

        return false;
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

        for (Skill skill: skillSet) {
            data.addSkillSet(skill.getData().build());
        }

        for (EmployeeSpecial specialus: employeeSpecials) {
            Proto.EmployeeSpecial.Builder builder = Proto.EmployeeSpecial.newBuilder();
            builder.setSpecial(specialus.getClass().getName());

            if (specialus.getClass() == Risky.class)
                builder.setLevel(((Risky)specialus).getLevel());

            data.addEmployeeSpecials(builder.build());
        }

        return data.build();
    }
}
