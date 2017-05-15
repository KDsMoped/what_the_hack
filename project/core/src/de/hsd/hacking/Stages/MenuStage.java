package de.hsd.hacking.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;

import de.hsd.hacking.UI.Button;
import de.hsd.hacking.UI.SimpleButton;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class MenuStage extends Stage {

    public static final float VIEWPORT_WIDTH = 256f;
    public static final float VIEWPORT_HEIGHT =  (Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / VIEWPORT_WIDTH));

    private Assets assets;
    private List<Button> buttons;

    private Vector2 touchVector;

    public MenuStage(Assets assets){
        super(new ExtendViewport(VIEWPORT_WIDTH ,VIEWPORT_HEIGHT));
        this.assets = assets;
        this.buttons = new ArrayList<Button>();
        this.touchVector = new Vector2();

        SimpleButton testButton = new SimpleButton(new Vector2(VIEWPORT_WIDTH / 2f, VIEWPORT_HEIGHT / 4f), "Test", assets, new Runnable() {
            @Override
            public void run() {
                //So kann man Console Prints machen
                Gdx.app.log(Constants.TAG, "TEST-CLICK!!!");
            }
        }
        );

        buttons.add(testButton);
        addActor(testButton);
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

        //x,y sind Screen-Koordinaten. viewport.unproject(x,y) kann man diese in das Viewport-Koordinatensystem projizieren
        touchVector.set(getViewport().unproject(touchVector.set(x,y)));
        for (Button b :
                buttons) {
            b.touchDown(touchVector);
        }

        return super.touchDown(x,y,pointer,button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button){

        touchVector.set(getViewport().unproject(touchVector.set(x,y)));
        for (Button b :
                buttons) {
            b.touchUp(touchVector);
        }

        return super.touchUp(x,y,pointer, button);
    }

    @Override
    public void dispose(){
       super.dispose();
    }

}
