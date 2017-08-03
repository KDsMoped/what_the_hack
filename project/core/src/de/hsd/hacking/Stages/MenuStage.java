package de.hsd.hacking.Stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.File;

import de.hsd.hacking.Assets.Assets;

import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.GameManager;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Utils.Constants;

/**
 * @author Florian, Julian
 */
public class MenuStage extends Stage {

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private AudioManager audioManager;

    private Animation<TextureRegion> backgroundAnim;
    private TextureRegion background_current;
    private float elapsedTime = 0f;

    public MenuStage(){
        super(Gdx.app.getType() == Application.ApplicationType.Android ? new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)
                : new FitViewport(512f, 288f));
        Assets assets = Assets.instance();
        this.audioManager = AudioManager.instance();

        backgroundAnim = new Animation<TextureRegion>(0.7f, assets.mainmenu_bg);


        TextButton resumeButton = new TextButton("Resume", Constants.TextButtonStyle());
        resumeButton.addListener(new ChangeListener() {
               @Override
               public void changed(ChangeEvent event, Actor actor) {
                   GameManager.instance().loadGame();
                   AudioManager.instance().playMenuButtonSound();
               }
           }
        );
        File f = new File(Gdx.files.getLocalStoragePath() + "/gametime");
        if (!f.exists()) {
            resumeButton.setVisible(false);
        }

        final TextButton newButton = new TextButton("New", Constants.TextButtonStyle());
        newButton.addListener(new ChangeListener() {
                               @Override
                               public void changed(ChangeEvent event, Actor actor) {
                                   GameManager.instance().newGame();
                                   AudioManager.instance().playMenuButtonSound();
                               }
                           }
        );

        resumeButton.setBounds(VIEWPORT_WIDTH / 2 - 97, VIEWPORT_HEIGHT - 220, 194, 40);
        addActor(resumeButton);

        newButton.setBounds(VIEWPORT_WIDTH / 2 - 97, VIEWPORT_HEIGHT - 265, 194, 40);
        addActor(newButton);
    }



    @Override
    public void act(float delta){
        super.act(delta);
        elapsedTime += delta;
        background_current = backgroundAnim.getKeyFrame(elapsedTime, true);
    }

    @Override
    public void draw(){
        Batch batch = getBatch();

        if (background_current != null){
            batch.begin();
            batch.draw(background_current, 0, VIEWPORT_HEIGHT - 400);
            batch.end();
        }

        super.draw();
        //Wenn ein batch außerhalb der draw-Methode eines Actors benutzt wird, muss dieser mit begin() und end() gestartet und beendet werden.
        //Wenn in einer draw Methode ein anderer Renderer (zB ShapeRenderer) verwendet werden soll,
        // muss batch auch erst beendet werden, dann der andere renderer gestartet/beendet, und am Ende muss der batch wieder gestartet werden.
        batch.begin();
        //assets.header_font.draw(getBatch(), "WHAT THE HACK", 0, VIEWPORT_HEIGHT, VIEWPORT_WIDTH, Align.center, false);
        batch.end();
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button){
        return super.touchDown(x,y,pointer,button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button){
        return super.touchUp(x,y,pointer, button);
    }

    @Override
    public void dispose(){
       super.dispose();
    }

}
