package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
    public BitmapFont gold_font;

    private TextureAtlas atlas;
    public TextureAtlas ui_atlas;

    public Array<TextureRegion> default_character_shadow;
    public Array<TextureRegion> default_character_legs;
    public Array<TextureRegion> default_character_body;
    public Array<TextureRegion> default_character_head;
    public Array<TextureRegion> default_character_hair;


    public Assets(){
        manager = new AssetManager();
    }

    public void load(){

        manager.load("img/Game_Assets.atlas", TextureAtlas.class);
        manager.load("img/UI_Assets.atlas", TextureAtlas.class);

        gold_font = new BitmapFont(Gdx.files.internal("fonts/upheaval_small.fnt"), Gdx.files.internal("fonts/small_gold_highlight.png"), false);

        //synchrones Laden. Für mehrere Assets nicht so gut.
        //Dann muss in der render-Methode des aktuellen Screens manager.update() aufgerufen werden.
        //Die Methode gibt dann true zurück wenn fertig geladen wurde. Erst danach dürfen die Referenzen gesetzt werden
        manager.finishLoading();

        atlas = manager.get("img/Game_Assets.atlas");
        ui_atlas = manager.get("img/UI_Assets.atlas");

        //BitmapFonts müssen per "Hiero" Tool erzeugt werden. Das Tool findet ihr auf der libgdx Seite.
        standard_font = new BitmapFont(Gdx.files.internal("fonts/test_font.fnt"), Gdx.files.internal("fonts/test_font.png"), false);
        header_font = new BitmapFont(Gdx.files.internal("fonts/test_font_big_white.fnt"), Gdx.files.internal("fonts/test_font_big_white.png"), false);
        //...//
        default_character_body = new Array<TextureRegion>(4);
        default_character_head = new Array<TextureRegion>(4);
        default_character_legs = new Array<TextureRegion>(4);
        default_character_hair = new Array<TextureRegion>(4);
        default_character_shadow = new Array<TextureRegion>(4);
        default_character_body.addAll(atlas.findRegions("character/Body"));
        default_character_shadow.addAll(atlas.findRegions("character/Shadow"));
        default_character_legs.addAll(atlas.findRegions("character/Legs"));
        default_character_hair.addAll(atlas.findRegions("character/Hair"));
        default_character_head.addAll(atlas.findRegions("character/Head"));


    }

    /*Muss von außerhalb aufgerufen werden wenn manager.update() true zurück gibt*/
    public void loadingDone(){

    }

    public void dispose(){
        manager.clear();
    }
}
