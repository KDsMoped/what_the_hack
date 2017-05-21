package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class Assets {

    public AssetManager manager;

    public BitmapFont standard_font;
    public BitmapFont header_font;

    private TextureAtlas atlas;

    public Array<TextureRegion> testButton;

    public Texture testTexture;


    public Assets(){
        manager = new AssetManager();
    }

    public void load(){
        manager.load("img/badlogic.jpg", Texture.class);
        manager.load("img/Game_Assets.atlas", TextureAtlas.class);

        //synchrones Laden. Für mehrere Assets nicht so gut.
        //Dann muss in der render-Methode des aktuellen Screens manager.update() aufgerufen werden.
        //Die Methode gibt dann true zurück wenn fertig geladen wurde. Erst danach dürfen die Referenzen gesetzt werden
        manager.finishLoading();

        testTexture = manager.get("img/badlogic.jpg");
        atlas = manager.get("img/Game_Assets.atlas");
        testButton = new Array<TextureRegion>(2);
        testButton.addAll(atlas.findRegions("test_button"));

        //BitmapFonts müssen per "Hiero" Tool erzeugt werden. Das Tool findet ihr auf der libgdx Seite.
        standard_font = new BitmapFont(Gdx.files.internal("fonts/test_font.fnt"), Gdx.files.internal("fonts/test_font.png"), false);
        header_font = new BitmapFont(Gdx.files.internal("fonts/test_font_big_white.fnt"), Gdx.files.internal("fonts/test_font_big_white.png"), false);
        //...//

    }

    /*Muss von außerhalb aufgerufen werden wenn manager.update() true zurück gibt*/
    public void loadingDone(){

    }

    public void dispose(){
        manager.clear();
    }
}
