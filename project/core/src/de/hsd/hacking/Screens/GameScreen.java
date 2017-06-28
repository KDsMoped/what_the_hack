package de.hsd.hacking.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.hsd.hacking.HackingGame;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

class GameScreen implements Screen {

    private HackingGame game;
    private Stage stage;

    public GameScreen(HackingGame game) {
        this.game = game;
        this.stage = new GameStage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Update the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
