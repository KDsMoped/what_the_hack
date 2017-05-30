package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Object;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class GameStage extends Stage {

    //Debug parameters
    private int employeeCount = 8;
    private int wallAmount = 5;

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));


    private TileMap tileMap;

    //TODO muss noch woanders hin gepackt werden.
    private List<Employee> employees;

    public GameStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.tileMap = new TileMap();
        addActor(this.tileMap);

        this.employees = new ArrayList<Employee>(4);

        while(employeeCount > 0){
            employees.add(new Employee(assets, Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMap, this));
            employeeCount--;
        }
        //CREATE WALLS TO TEST A* PATHFINDING
        while (wallAmount > 0){
            tileMap.getFreeTile().setObject(new Object(this, true));
            wallAmount--;
        }
    }


    @Override
    public void draw() {
        Collections.sort(employees);
        super.draw();
        Batch batch = getBatch();
        batch.begin();
        for (Employee em :
                employees) {
            em.draw(batch, 1f);
        }
        batch.end();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (Employee em :
                employees) {
            em.act(delta);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
