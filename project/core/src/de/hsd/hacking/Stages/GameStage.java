package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Entities.Objects.Equipment.CoffeeMachine;
import de.hsd.hacking.Entities.Objects.Equipment.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Entities.Objects.ObjectFactory;
import de.hsd.hacking.Entities.Objects.ObjectType;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.UI.Employee.EmployeeBar;
import de.hsd.hacking.UI.MissionBrowser;
import de.hsd.hacking.UI.Shop.ShopBrowser;
import de.hsd.hacking.UI.StatusBar;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class GameStage extends Stage {

    private Assets assets;
    //Debug parameters
    private int frames = 0;
    private int framesCount;
    private float elapsedTime = 0f;

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT = (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Vector2 checkVector;
    private TileMap tileMap;
    private Team team;
    private StatusBar statusBar;

    private List<Touchable> touchables;

    private final MissionBrowser missionBrowser = new MissionBrowser();

    private Group foreground, background, ui, popups;

    public GameStage() {
        super(new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "WIDTH: " + VIEWPORT_WIDTH + ", HEIGHT: " + VIEWPORT_HEIGHT);
        this.checkVector = new Vector2();
        this.assets = Assets.instance();
        this.tileMap = new TileMap(this);

        InitRootObjects();
        InitTeam();
        InitInterior();
        InitUI();

        // REMOVE THIS AGAIN
        if (Constants.DEBUG) {
            Skin uiSkin = new Skin(assets.ui_atlas);
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(uiSkin.getDrawable("win32_button_9_patch_normal"), uiSkin.getDrawable("win32_button_9_patch_pressed"),
                    null, assets.status_bar_font);
            style.fontColor = Color.BLACK;
            style.pressedOffsetY = -1f;
            style.pressedOffsetX = 1f;

            TextButton upgradeButton = new TextButton("Upgrade", style);
            upgradeButton.addListener(new ChangeListener() {
                                          @Override
                                          public void changed(ChangeEvent event, Actor actor) {
                                              ArrayList<Equipment> equipments = team.getEquipmentList();
                                              for (Equipment equipment :
                                                      equipments) {
                                                  if (equipment instanceof Upgradable) {
                                                      ((Upgradable) equipment).upgrade();
                                                  }
                                              }
                                          }
                                      }
            );
            upgradeButton.setBounds(10, 40, 100, 30);

            popups.addActor(upgradeButton);
        }
    }

    private void InitRootObjects() {
        foreground = new Group();
        background = new Group();
        ui = new Group();
        popups = new Group();
        touchables = new ArrayList<Touchable>();

        // the order the actors are added is important
        // it is also the drawing order
        // meaning the last added item will also be drawn last
        addActor(background);
        addActor(this.tileMap);
        addActor(foreground);
        addActor(ui);
        addActor(popups);

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
        createWorkSpace(Constants.TILES_PER_SIDE / 4, Constants.TILES_PER_SIDE / 3);
        createWorkSpace((Constants.TILES_PER_SIDE / 4) * 3, Constants.TILES_PER_SIDE / 3);
        createWorkSpace((Constants.TILES_PER_SIDE / 4), (Constants.TILES_PER_SIDE / 3) * 2);
        createWorkSpace((Constants.TILES_PER_SIDE / 4) * 3, (Constants.TILES_PER_SIDE / 3) * 2);

        //other interior
        Desk desk = new Desk(assets, Direction.RIGHT, 1);
        tileMap.addObject(10, 0, desk);
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        desk.setContainedObject(coffeeMachine);
        addTouchable(coffeeMachine);
    }

    private void InitUI() {
        int buttonHeight = 20;
        int buttonSpacing = 5;

        //Init Mission Window
        popups.addActor(missionBrowser);

        //Init Shop button
        final ShopBrowser shopBrowser = new ShopBrowser();
        TextButton shopButton = new TextButton("Shop", Constants.TextButtonStyle());
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (shopBrowser.isActive()) {
                    shopBrowser.Close();
                } else {
                    shopBrowser.Show();
                }
            }
        });
        shopButton.setBounds(0, VIEWPORT_HEIGHT - buttonHeight, 100, buttonHeight);
        ui.addActor(shopButton);
        ui.addActor(shopBrowser);

        //Init Missions button
        TextButton jobsButton = new TextButton("Jobs", Constants.TextButtonStyle());
        jobsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (missionBrowser.isActive()) {
                    missionBrowser.Close();
                } else {
                    missionBrowser.Show();
                }
            }
        });
        jobsButton.setBounds(0, VIEWPORT_HEIGHT - 2 * buttonHeight, 100, buttonHeight);
        ui.addActor(jobsButton);

        //Init Recruitment button
        TextButton recruitmentButton = new TextButton("Recruit", Constants.TextButtonStyle());
        recruitmentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //
            }
        });
        recruitmentButton.setBounds(0, VIEWPORT_HEIGHT - 3 * buttonHeight, 100, buttonHeight);
        ui.addActor(recruitmentButton);

        //Init Exit button
        TextButton exitButton = new TextButton("Exit", Constants.TextButtonStyle());
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.setMenuScreen();
            }
        });
        exitButton.setBounds(VIEWPORT_WIDTH - 100, VIEWPORT_HEIGHT - buttonHeight, 100, buttonHeight);
        ui.addActor(exitButton);

        //Init status bar & employee details
        ui.addActor(statusBar = new StatusBar());
        ui.addActor(new EmployeeBar());
    }

    private void createWorkSpace(int tileX, int tileY) {
        Desk desk = new Desk(assets, Direction.RIGHT, 1);
        tileMap.addObject(tileX, tileY, desk);
        Chair chair = new Chair(assets);
        tileMap.addObject(tileX, tileY - 1, chair);
        Computer computer = new Computer();
        computer.setWorkingChair(chair);
        addTouchable(computer);
        desk.setContainedObject(computer);
    }

    private void InitTeam() {

        team = Team.instance();
        team.initialize(this);

        while (true) {
            int ret = team.createAndAddEmployee(Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMap);
            if (ret != 0) {
                break;
            }
        }

        team.createAndAddEquipment(EquipmentType.MODEM);
        touchables.addAll(team.getEmployeeList());
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
        for (Employee em : team.getEmployeeList()) {
            em.act(delta);
        }

        if (Constants.DEBUG) {
            framesCount++;
            if (elapsedTime >= 1f) {
                elapsedTime = 0f;
                frames = framesCount;
                framesCount = 0;
                this.tileMap.debugCheck(team.getEmployeeCount());
            }
        }
        //team.calcRessorces();
        statusBar.setMoney(team.getMoney());
        statusBar.setBandwith(team.getBandwith());
        statusBar.setWorkplaces(team.getWorkspaceCount());
        statusBar.setEmployees(team.getEmployeeCount());

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!super.touchDown(screenX, screenY, pointer, button)) {
            getViewport().unproject(checkVector.set(screenX, screenY));
            boolean touchableTouched = false;
            if (pointer == 0) {
                for (Touchable touchable :
                        touchables) {
                    if (touchable.touchDown(checkVector)) {
                        touchableTouched = true;
                        break;
                    }
                }
            }
            return touchableTouched;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!super.touchUp(screenX, screenY, pointer, button)) {
            getViewport().unproject(checkVector.set(screenX, screenY));
            boolean touchableTouchedUp = false;
            if (pointer == 0) {
                for (Touchable touchable :
                        touchables) {
                    if (touchable.touchUp(checkVector)) {
                        touchableTouchedUp = true;
                        break;
                    }
                }
            }
            return touchableTouchedUp;
        }
        return true;
    }

    public boolean addTouchable(Touchable touchable) {
        if (touchables.contains(touchable)) {
            return false;
        }
        touchables.add(touchable);
        return true;
    }

    public void addTouchables(Collection<Touchable> touchables) {
        this.touchables.addAll(touchables);
    }

    public boolean removeTouchable(Touchable touchable) {
        return touchables.remove(touchable);
    }

    public Employee getSelectedEmployee() {
        return team.getSelectedEmployee();
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        team.setSelectedEmployee(selectedEmployee);
    }

    public void deselectEmployee() {
        team.deselectEmployee();
    }

    @Override
    public void dispose(){
//        super.dispose();
    }
}
