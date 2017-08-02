package de.hsd.hacking.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import de.hsd.hacking.HackingGame;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.UI.General.SimpleGestureDetector;
import de.hsd.hacking.Utils.Constants;

class GameScreen implements Screen {

    private GameStage stage;
    private Runnable swipeUpAction;

    public GameScreen(HackingGame game) {
        HackingGame game1 = game;
        this.stage = new GameStage();

        InputMultiplexer multiplexer = new InputMultiplexer();



        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new SimpleGestureDetector(new SimpleGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                Gdx.app.log(Constants.TAG, "SWIPE UP");

                if (swipeUpAction != null) {
                    swipeUpAction.run();
                }
            }

            @Override
            public void onRight() {

            }

            @Override
            public void onLeft() {

            }

            @Override
            public void onDown() {

            }
        }));
        Gdx.input.setInputProcessor(multiplexer);
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
        stage.getViewport().update(width, height);
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

    public void setSwipeUpAction(Runnable r) {
        swipeUpAction = r;
    }
}
