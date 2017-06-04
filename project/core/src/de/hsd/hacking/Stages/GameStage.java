package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Object;
import de.hsd.hacking.Entities.Team.Team;
import com.badlogic.gdx.scenes.scene2d.Group;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class GameStage extends Stage {

    private Assets assets;
    //Debug parameters
    private int employeeCount = 4;

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Vector2 checkVector;
    private TileMap tileMap;
    private Team team;

    private List<Touchable> touchables;
    
    public GameStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.checkVector = new Vector2();
        this.assets = assets;
        this.tileMap = new TileMap();
        addActor(this.tileMap);

        team = new Team(this);
        this.touchables = new ArrayList<Touchable>(4);

        while (0 == 0) {
            int ret = team.createAndAddEmployee(assets, Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMap);
            if (ret != 0) { break; }
        }
        
        this.touchables.addAll(team.getEmployeeList());
        //CREATE WALLS TO TEST A* PATHFINDING

        tileMap.getTiles()[0][0].setObject(new Object(this, true));
        tileMap.getTiles()[0][1].setObject(new Object(this, true));
        tileMap.getTiles()[0][2].setObject(new Object(this, true));
        tileMap.getTiles()[1][0].setObject(new Object(this, true));
        tileMap.getTiles()[2][0].setObject(new Object(this, true));
        tileMap.getTiles()[1][1].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 1].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 2].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 1][Constants.TILES_PER_SIDE - 3].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 2][Constants.TILES_PER_SIDE - 1].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 3][Constants.TILES_PER_SIDE - 1].setObject(new Object(this, true));
        tileMap.getTiles()[Constants.TILES_PER_SIDE - 2][Constants.TILES_PER_SIDE - 2].setObject(new Object(this, true));
    }


    @Override
    public void draw() {
        ArrayList<Employee> employees = team.getEmployeeList();
        Collections.sort(employees);
        Batch batch = getBatch();
        batch.begin();
        batch.draw(assets.room_bg, 0, 0);
        batch.end();
        super.draw();
        batch.begin();
        for (Employee em :
                employees) {
            em.draw(batch, 1f);
        }
        batch.draw(assets.room_fg, 0, 0);
        batch.end();
    }

    @Override
    public void act(float delta) {
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
