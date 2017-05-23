package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
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

    public MenuStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.assets = assets;

        Skin uiSkin = new Skin(assets.ui_atlas);


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(uiSkin.getDrawable("Button_9_Patch_normal"), uiSkin.getDrawable("Button_9_Patch_pressed"),
                null, assets.gold_font);
        style.pressedOffsetY = -5f;
        TextButton button = new TextButton("START", style);
        button.addListener(new ChangeListener() {
               @Override
               public void changed(ChangeEvent event, Actor actor) {
                   ScreenManager.setGameScreen();
               }
           }
        );
        button.setBounds(VIEWPORT_WIDTH * 0.3f, VIEWPORT_HEIGHT * 0.1f, VIEWPORT_WIDTH * 0.3f, VIEWPORT_HEIGHT * 0.3f);
        addActor(button);
    }

    @Override
    public void act(float deltaTime){
        super.act(deltaTime);
    }

    @Override
    public void draw(){
        super.draw();
        Batch batch = getBatch();
        //Wenn ein batch außerhalb der draw-Methode eines Actors benutzt wird, muss dieser mit begin() und end() gestartet und beendet werden.
        //Wenn in einer draw Methode ein anderer Renderer (zB ShapeRenderer) verwendet werden soll,
        // muss batch auch erst beendet werden, dann der andere renderer gestartet/beendet, und am Ende muss der batch wieder gestartet werden.
        batch.begin();
        assets.header_font.draw(getBatch(), "HACKINGGAME", 0, VIEWPORT_HEIGHT, VIEWPORT_WIDTH, Align.center, false);
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
