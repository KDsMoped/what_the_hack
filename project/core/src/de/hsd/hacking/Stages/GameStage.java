package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Data.TileMap;
import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class GameStage extends Stage {

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Assets assets;
    private MovementProvider tileMovementProvider;

    private ShapeRenderer debugBGRenderer;

    public GameStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.assets = assets;
        this.debugBGRenderer = new ShapeRenderer();
        this.tileMovementProvider = new TileMap();
        Group employees = new Group();
        addActor(employees);
        employees.addActor(new Employee(assets, Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMovementProvider));
        employees.addActor(new Employee(assets, Employee.EmployeeSkillLevel.getRandomSkillLevel(), this.tileMovementProvider));
    }


    @Override
    public void draw() {
        Batch batch = getBatch();
        debugBGRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        debugBGRenderer.setTransformMatrix(batch.getTransformMatrix());
        debugBGRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugBGRenderer.setColor(Color.GRAY);
        debugBGRenderer.rect(0, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        debugBGRenderer.end();

        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
