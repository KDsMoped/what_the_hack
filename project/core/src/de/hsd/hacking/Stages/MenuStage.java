package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.hsd.hacking.Assets.Assets;

import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class MenuStage extends Stage {

    public static final float VIEWPORT_WIDTH = 512f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Assets assets;

    private Animation<TextureRegion> background_anim;
    private TextureRegion background_current;
    private float elapsedTime = 0f;

    public MenuStage(){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.assets = Assets.instance();

        background_anim = new Animation<TextureRegion>(0.7f, assets.mainmenu_bg);


        TextButton button = new TextButton("what_the_hack.exe", Constants.TextButtonStyle());
        button.addListener(new ChangeListener() {
               @Override
               public void changed(ChangeEvent event, Actor actor) {
                   assets.buttonSound.play();
                   ScreenManager.setGameScreen();
               }
           }
        );

        button.setBounds(VIEWPORT_WIDTH / 2 - 97, VIEWPORT_HEIGHT - 220, 194, 40);
        addActor(button);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        elapsedTime += delta;
        background_current = background_anim.getKeyFrame(elapsedTime, true);
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
