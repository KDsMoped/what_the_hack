package de.hsd.hacking.Stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Data.SaveGameContainer;
import de.hsd.hacking.Data.Tile.TileMap;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Entities.Team.Workspace;
import de.hsd.hacking.GameManager;
import de.hsd.hacking.UI.Employee.EmployeeBrowser;
import de.hsd.hacking.UI.General.AudioTextButton;
import de.hsd.hacking.UI.Messaging.MessageBar;
import de.hsd.hacking.UI.Mission.MissionStatusOverlay;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.ObjectFactory;
import de.hsd.hacking.Entities.Objects.ObjectType;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.UI.Employee.EmployeeBar;
import de.hsd.hacking.UI.Mission.MissionBrowser;
import de.hsd.hacking.UI.Shop.ShopBrowser;
import de.hsd.hacking.UI.General.StatusBar;
import de.hsd.hacking.Utils.Constants;

/**
 * @author Florian, Hendrik, Julian
 */
public class GameStage extends Stage implements EventListener{

    private Assets assets;
    //Debug parameters
    private int frames = 0;
    private int framesCount;
    private float elapsedTime = 0f;

    public static float VIEWPORT_WIDTH = 512f;
    public static float VIEWPORT_HEIGHT = 288f;
            //(Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Vector2 checkVector;
    private TileMap tileMap;
    private TeamManager teamManager;
    private EmployeeManager employeeManager;
    private StatusBar statusBar;
    private MessageBar messageBar;
    private MissionStatusOverlay missionStatusOverlay;

    private List<Touchable> touchables;

    private List<Workspace> workspaces;

    private final MissionBrowser missionBrowser = new MissionBrowser();

    private Group ui;
    private Group popups;
    private Group overlay;

    private static GameStage instance;
    private boolean employeesTouchable = true;
    private EmployeeBar employeeBar;

    private SaveGameContainer saveGameContainer;

    public static GameStage instance() {

        return instance;
    }

    public GameStage() {
        super(
               /* Gdx.app.getType() == Application.ApplicationType.Android ? new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)
        : */
                        new FitViewport(512f, VIEWPORT_HEIGHT = 288f));
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "WIDTH: " + VIEWPORT_WIDTH + ", HEIGHT: " + VIEWPORT_HEIGHT);
        instance = this;
        this.checkVector = new Vector2();
        this.assets = Assets.instance();
        this.tileMap = new TileMap(this);

        //TODO mit gespeicherten Werten aufrufen

        InitRootObjects();
        InitInterior();
        InitEquipment();
        InitTeam();
        InitUI();
        InitSaveGameList();

        AudioManager.instance().playMusic();
    }

    private void InitRootObjects() {
        Group foreground = new Group();
        Group background = new Group();
        ui = new Group();
        popups = new Group();
        overlay = new Group();
        touchables = new ArrayList<Touchable>();
        workspaces = new ArrayList<Workspace>();

        // the order the actors are added is important
        // it is also the drawing order
        // meaning the last added item will also be drawn last
        addActor(GameTime.instance());
        addActor(background);
        addActor(tileMap);
        addActor(foreground);
        addActor(ui);
        addActor(popups);
        addActor(overlay);

        foreground.addActor(new Image(assets.room_fg));
        background.addActor(new Image(assets.room_bg));
    }

    private void InitInterior() {
        //CREATE WALLS TO TEST A* PATHFINDING
        tileMap.addObject(0, 0, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(0, 1, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(0, 2, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(1, 0, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(2, 0, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(1, 1, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 1, Constants.TILES_PER_SIDE - 1, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 1, Constants.TILES_PER_SIDE - 3, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 1, Constants.TILES_PER_SIDE - 3, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 2, Constants.TILES_PER_SIDE - 1, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 3, Constants.TILES_PER_SIDE - 1, ObjectFactory.generateObject(ObjectType.WALL, assets));
        tileMap.addObject(Constants.TILES_PER_SIDE - 2, Constants.TILES_PER_SIDE - 2, ObjectFactory.generateObject(ObjectType.WALL, assets));

        //populate room with objects
        tileMap.addObject(3, 0, ObjectFactory.generateObject(ObjectType.LAMP, assets));
        tileMap.addObject(0, 3, ObjectFactory.generateObject(ObjectType.LAMP, assets));

        //Workspaces
        Workspace workspace1 = new Workspace(tileMap, Constants.TILES_PER_SIDE / 4, Constants.TILES_PER_SIDE / 3);
        Workspace workspace2 = new Workspace(tileMap, (Constants.TILES_PER_SIDE / 4) * 3, Constants.TILES_PER_SIDE / 3);
        Workspace workspace3 = new Workspace(tileMap, (Constants.TILES_PER_SIDE / 4), (Constants.TILES_PER_SIDE / 3) * 2);
        Workspace workspace4 = new Workspace(tileMap, (Constants.TILES_PER_SIDE / 4) * 3, (Constants.TILES_PER_SIDE / 3) * 2);
        workspaces.add(workspace1);
        workspaces.add(workspace2);
        workspaces.add(workspace3);
        workspaces.add(workspace4);
    }

    private void InitEquipment() {
        EquipmentManager.instance().placeExistingEquipment();
    }

    private void InitUI() {


        this.employeeBar = new EmployeeBar();
        popups.addActor(employeeBar);

        missionStatusOverlay = new MissionStatusOverlay();
        missionStatusOverlay.setVisible(false);
        popups.addActor(missionStatusOverlay);
        GameTime.instance().addTimeChangedListener(missionStatusOverlay);

        //Init Mission Window
        popups.addActor(missionBrowser);


        InitLeftMenuTable();




        //Init status bar, message bar & employee details
        overlay.addActor(statusBar = new StatusBar());
        overlay.addActor(messageBar = new MessageBar());
        GameTime.instance().addTimeChangedListener(statusBar);
    }

    private void InitLeftMenuTable(){
        int buttonHeight = 32;
        int buttonWidth = 32;
        int buttonSpacing = 0;

//        Init Shop button
        final ShopBrowser shopBrowser = new ShopBrowser();
        AudioTextButton shopButton = new AudioTextButton("", Constants.TerminalButtonStyle());
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TeamManager.instance().deselectEmployee();
                shopBrowser.toggleView();
            }
        });
        shopButton.setBounds(0, VIEWPORT_HEIGHT - buttonHeight, buttonWidth, buttonHeight);
        shopButton.add(new Image(assets.ui_icon_shop));
        ui.addActor(shopButton);
        popups.addActor(shopBrowser);


        //Init Missions button
        AudioTextButton jobsButton = new AudioTextButton("", Constants.TerminalButtonStyle());
        jobsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TeamManager.instance().deselectEmployee();
                missionBrowser.toggleView();
            }
        });
        jobsButton.setBounds(0, VIEWPORT_HEIGHT - 2 * buttonHeight - buttonSpacing, buttonWidth, buttonHeight);
        jobsButton.add(new Image(assets.ui_icon_jobs));
        ui.addActor(jobsButton);

        //Init Recruitment button
        final EmployeeBrowser employeeBrowser = new EmployeeBrowser();
        AudioTextButton recruitmentButton = new AudioTextButton("", Constants.TerminalButtonStyle());
        recruitmentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TeamManager.instance().deselectEmployee();
                employeeBrowser.toggleView();
            }
        });
        recruitmentButton.setBounds(0, VIEWPORT_HEIGHT - 3 * buttonHeight - 2 * buttonSpacing, buttonWidth, buttonHeight);
        recruitmentButton.add(new Image(assets.ui_icon_team));
        ui.addActor(recruitmentButton);
        popups.addActor(employeeBrowser);

        //Init Exit button
        AudioTextButton exitButton = new AudioTextButton("", Constants.TerminalButtonStyle());
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance().exitGame();
            }
        });
        exitButton.setBounds(VIEWPORT_WIDTH - buttonWidth, VIEWPORT_HEIGHT - buttonHeight, buttonWidth, buttonHeight);
        exitButton.add(new Image(assets.ui_icon_exit));
        ui.addActor(exitButton);


//        Table leftMenuTable = new Table();
//
//        final ShopBrowser shopBrowser = new ShopBrowser();
//        ImageButton shopButton = new ImageButton()
//
//        ui.addActor(leftMenuTable);
    }


    private void InitTeam() {
        employeeManager = EmployeeManager.instance();
        teamManager = TeamManager.instance();

        for (Employee e : employeeManager.getHiredEmployees()) {
            e.initEmployeePosition();
            e.restoreWorkingState();
        }

        EquipmentManager.instance().initBasicEquipment();
    }

    private void InitSaveGameList() {
        saveGameContainer = new SaveGameContainer();
        saveGameContainer.messageBar = this.messageBar;
    }

    @Override
    public void draw() {

        Batch batch = getBatch();
        super.draw();
        batch.begin();
        if (Constants.DEBUG) {
            Assets.gold_font_small.draw(batch, "" + frames, VIEWPORT_WIDTH - 20f, 20f);
        }
        batch.end();
    }

    @Override
    public void act(float delta) {
        delta = MathUtils.clamp(delta, 0f, .05f);
        elapsedTime += delta;
        super.act(delta);
        for (Employee em : EmployeeManager.instance().getHiredEmployees()) {
            em.act(delta);
        }

        if (Constants.DEBUG) {
            framesCount++;
            if (elapsedTime >= 1f) {
                elapsedTime = 0f;
                frames = framesCount;
                framesCount = 0;
                this.tileMap.debugCheck(employeeManager.getTeamSize());
            }
        }
        //team.calcRessorces();
        statusBar.setMoney(TeamManager.instance().resources.getMoney());
        statusBar.setBandwith(TeamManager.instance().resources.getBandwidth());
        statusBar.setWorkplaces(TeamManager.instance().getWorkspaceCount());
        statusBar.setEmployees(employeeManager.getTeamSize());

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        getViewport().unproject(checkVector.set(screenX, screenY));
        if (pointer == 0) {
            if (!super.touchDown(screenX, screenY, pointer, button)) {
                    for (Employee em
                            : employeeManager.getHiredEmployees()) {
                        if (em.touchDown(checkVector)) {
                            return true;
                        }
                    }
                for (Touchable touchable
                        : touchables) {
                    if (touchable.touchDown(checkVector)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        getViewport().unproject(checkVector.set(screenX, screenY));

        //We allow only 1 finger on screen
        if (pointer == 0) {
            //1st priority: UI elements
            if (!super.touchUp(screenX, screenY, pointer, button)) {
            //2nd priority: Employees
                for (Employee em
                        : employeeManager.getHiredEmployees()) {
                    if (em.touchUp(checkVector)) {
                        return true;
                    }
                }
                //3rd priority: Objects
                for (Touchable touchable
                        : touchables) {
                    if (touchable.touchUp(checkVector)) {
                        return true;
                    }
                }

                //If no touchable object is clicked, deselect the current employee
                if (TeamManager.instance().isEmployeeSelected()) {
                    TeamManager.instance().getSelectedEmployee().setSelected(false);
                }

                if (employeeBar.isEmployeeProfileOpen()) {
                    employeeBar.closeEmployeeProfile();
                }

            } else {
                return true;
            }
        }
        return false;
    }

    private boolean popUpOpen() {
        return false;
    }

    public boolean addTouchable(final Touchable touchable) {
        if (touchables.contains(touchable)) {
            return false;
        }
        touchables.add(touchable);
        return true;
    }


    public void addTouchables(Collection<Touchable> touchables) {
        this.touchables.addAll(touchables);
    }

    public void addToUILayer(final Actor actor) {
        ui.addActor(actor);
    }

    public boolean removeTouchable(Touchable touchable) {
        return touchables.remove(touchable);
    }


    public TileMap getTileMap() {
        return tileMap;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void addPopup(Actor actor) {
        popups.addActor(actor);
    }


    @Override
    public void OnEvent(EventType type, Object sender) {
        switch (type) {
            // Fun fact: Ich glaube so etwas hat mal zu einem Bug für einen iOS Jailbreak geführt :D
            case MISSION_STARTED:
            case MISSION_FINISHED:
            case MISSION_ABORTED:
            case MESSAGE_NEW:
                break;
        }
    }

    public void showMissionStatusOverlay(MissionWorker missionWorker, Employee employee) {
        missionStatusOverlay.setPosition(employee.getPosition().add(32f, -16f));
        missionStatusOverlay.setMissionWorker(missionWorker);
        missionStatusOverlay.setVisible(true);
    }

    public void hideMissionStatusOverlay() {
        missionStatusOverlay.setVisible(false);
        missionStatusOverlay.setMissionWorker(null);
    }

    public List<Workspace> getWorkspaces() {return workspaces; }

    public SaveGameContainer getSaveGameContainer() {
        return saveGameContainer;
    }

    /**
     * Destroys this manager instance.
     */
    public void cleanUp() {
        instance = null;
    }
}
