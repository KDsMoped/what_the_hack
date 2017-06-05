package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class Assets {

    public AssetManager manager;

    public BitmapFont standard_font;
    public BitmapFont header_font;
    public BitmapFont gold_font;
    public BitmapFont gold_font_small;
    public BitmapFont status_bar_font;

    private TextureAtlas atlas;
    public TextureAtlas ui_atlas;
    private TextureAtlas character_atlas;

    public TextureRegion room_bg;
    public TextureRegion room_fg;

    public Array<TextureRegion> gray_character_body;
    public Array<TextureRegion> hair_01;
    public Array<TextureRegion> hair_02;

    public TextureRegionDrawable bandwith_icon, money_icon, employees_icon, clock_icon;

    public Sound buttonSound;

    public Assets(){
        manager = new AssetManager();
    }

    public void load(){

        //manager.load queued assets zum Laden. Das Laden muss aber noch manuell angestoßen werden. Der String Pfad ist später der Key um die Ressource per "manager.get("path")" zu erhalten
        manager.load("img/Game_Assets.atlas", TextureAtlas.class);
        manager.load("img/UI_Assets.atlas", TextureAtlas.class);
        manager.load("img/Character_Assets.atlas", TextureAtlas.class);

        manager.load("sounds/dummy_button.wav", Sound.class);

        gold_font = new BitmapFont(Gdx.files.internal("fonts/upheaval_small.fnt"), Gdx.files.internal("fonts/small_gold_highlight.png"), false);
        gold_font_small = new BitmapFont(Gdx.files.internal("fonts/upheaval_small.fnt"), Gdx.files.internal("fonts/small_gold_highlight.png"), false);
        gold_font_small.getData().setScale(.5f);

        //synchrones Laden. Für mehrere Assets nicht so gut.
        //Dann muss in der render-Methode des aktuellen Screens manager.update() aufgerufen werden.
        //Die Methode gibt dann true zurück wenn fertig geladen wurde. Erst danach dürfen die Referenzen gesetzt werden
        manager.finishLoading();

        atlas = manager.get("img/Game_Assets.atlas");
        ui_atlas = manager.get("img/UI_Assets.atlas");
        character_atlas = manager.get("img/Character_Assets.atlas");

        buttonSound = manager.get("sounds/dummy_button.wav");

        //BitmapFonts müssen per "Hiero" Tool erzeugt werden. Das Tool findet ihr auf der libgdx Seite.
        standard_font = new BitmapFont(Gdx.files.internal("fonts/test_font.fnt"), Gdx.files.internal("fonts/test_font.png"), false);
        header_font = new BitmapFont(Gdx.files.internal("fonts/test_font_big_white.fnt"), Gdx.files.internal("fonts/test_font_big_white.png"), false);
        status_bar_font = new BitmapFont(Gdx.files.internal("fonts/status_bar_font.fnt"), Gdx.files.internal("fonts/status_bar_font.png"), false);
        //...//

        room_bg = atlas.findRegion("ambient/Room_Background");
        room_fg = atlas.findRegion("ambient/Room_FrontWall");

        gray_character_body = new Array<TextureRegion>();
        hair_01 = new Array<TextureRegion>();
        hair_02 = new Array<TextureRegion>();
        gray_character_body.addAll(character_atlas.findRegions("Char"));
        hair_01.addAll(character_atlas.findRegions("Hair01"));
        hair_02.addAll(character_atlas.findRegions("Hair02"));

        bandwith_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_bandwidth"));
        money_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_money"));
        employees_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_employees"));
        clock_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_clock"));
    }

    /*Muss von außerhalb aufgerufen werden wenn manager.update() true zurück gibt*/
    public void loadingDone(){

    }

    public void dispose(){
        character_atlas.dispose();
        ui_atlas.dispose();
        atlas.dispose();
        manager.clear();

    }
}
