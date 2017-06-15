package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Equipment.Computer;
import de.hsd.hacking.Entities.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Lamp;
import de.hsd.hacking.Entities.Objects.Object;
import de.hsd.hacking.Entities.Objects.Wall;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Entities.Tile;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.UI.EmployeeProfile;
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
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Vector2 checkVector;
    private TileMap tileMap;
    private Team team;

    private List<Touchable> touchables;

    private Group foreground, background, popups;
    
    public GameStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        Gdx.app.log(Constants.TAG, "WIDTH: " + VIEWPORT_WIDTH + ", HEIGHT: " + VIEWPORT_HEIGHT);
        this.checkVector = new Vector2();
        this.assets = assets;
        this.tileMap = new TileMap();

        foreground = new Group();
        background = new Group();
        popups = new Group();

        // the order the actors are added is important
        // it is also the drawing order
        // meaning the last added item will also be drawn last
        addActor(background);
        addActor(this.tileMap);
        addActor(new StatusBar(assets));
        addActor(foreground);
        addActor(popups);

        foreground.addActor(new Image(assets.room_fg));
        background.addActor(new Image(assets.room_bg));

        team = Team.getInstance();
        team.initialize(this);
        this.touchables = new ArrayList<Touchable>(4);

        //CREATE WALLS TO TEST A* PATHFINDING
        tileMap.getTiles()[0][0].setObject(new Wall());
        tileMap.getTiles()[0][1].setObject(new Wall());
        tileMap.getTiles()[0][2].setObject(new Wall());
        tileMap.getTiles()[1][0].setObject(new Wall());
        tileMap.getTiles()[2][0].setObject(new Wall());
        tileMap.getTiles()[1][1].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 1].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 2].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 3].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 2][Constants.TILES_PER_SIDE - 1].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 3][Constants.TILES_PER_SIDE - 1].setObject(new Wall());
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 2][Constants.TILES_PER_SIDE - 2].setObject(new Wall());
        tileMap.getTiles()[3][0].setObject(new Lamp(assets));
        Desk desk = new Desk(assets, Direction.RIGHT, 1);
        tileMap.getTiles()[Constants.TILES_PER_SIDE / 2][Constants.TILES_PER_SIDE / 2].setObject(desk);
        desk.setContainedObject(new Computer(0f, Equipment.EquipmentAttributeLevel.LOW, assets));

        while (true) {
            int ret = team.createAndAddEmployee(assets, Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMap);
            if (ret != 0) { break; }
        }
        
        this.touchables.addAll(team.getEmployeeList());

        // REMOVE THIS AGAIN
        if (Constants.DEBUG) {
            final EmployeeProfile popup = new EmployeeProfile(assets);


            Skin uiSkin = new Skin(assets.ui_atlas);
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(uiSkin.getDrawable("win32_button_9_patch_normal"), uiSkin.getDrawable("win32_button_9_patch_pressed"),
                    null, assets.status_bar_font);
            style.fontColor = Color.BLACK;
            style.pressedOffsetY = -1f;
            style.pressedOffsetX = 1f;
            TextButton button = new TextButton("Popup", style);
            button.addListener(new ChangeListener() {
                                   @Override
                                   public void changed(ChangeEvent event, Actor actor) {
                                       if (popup.getActive()){
                                           popup.Close();
                                       }
                                       else {
                                           popup.Show();
                                       }
                                   }
                               }
            );
            button.setBounds(10, 10, 100, 30);

            popups.addActor(button);
            popups.addActor(popup);
        }
    }


    @Override
    public void draw() {
        ArrayList<Employee> employees = team.getEmployeeList();
        tileMap.clearPassersBy();
        for (Employee employee :
                employees) {
            if (employee.getAnimationState() == Employee.AnimState.MOVING){
                Tile tile = tileMap.getTile(employee.getPosition().cpy().add(16f, 8f)); //TODO tilemap.getTile verbessern
                tile.addPasserBy(employee);
            }
        }

        Batch batch = getBatch();
        super.draw();
        batch.begin();
        if (Constants.DEBUG){
            assets.gold_font_small.draw(batch, "" + frames, VIEWPORT_WIDTH - 20f, 20f);
        }
        batch.end();
    }

    @Override
    public void act(float delta) {
        elapsedTime += delta;
        framesCount++;
        if (elapsedTime >= 1f){
            elapsedTime = 0f;
            frames = framesCount;
            framesCount = 0;
        }
        super.act(delta);
        for (Employee em :
                team.getEmployeeList()) {
            em.act(delta);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        getViewport().unproject(checkVector.set(screenX, screenY));
        if (pointer == 0){
            for (Touchable touchable :
                    touchables) {
                touchable.touchDown(checkVector);
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        getViewport().unproject(checkVector.set(screenX, screenY));
        if (pointer == 0) {
            for (Touchable touchable :
                    touchables) {
                touchable.touchUp(checkVector);
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
