package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

import de.hsd.hacking.Entities.Employees.Employee;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class Assets {

    public AssetManager manager;

    public static BitmapFont standard_font;
    public BitmapFont header_font;
    public BitmapFont gold_font;
    public static BitmapFont gold_font_small;
    public BitmapFont status_bar_font;

    private TextureAtlas atlas;
    public TextureAtlas ui_atlas;
    private TextureAtlas character_atlas;

    public TextureRegion room_bg;
    public TextureRegion room_fg;
    public TextureRegion lamp;
    public TextureRegion desk_1;
    public TextureRegion desk_2;
    public TextureRegion chair;

    public Array<TextureRegion> floor_tiles;
    public Array<TextureRegion> gray_character_body;
    public Array<TextureRegion> hair_01;
    public Array<TextureRegion> hair_02;
    public Array<TextureRegion> computer;

    public TextureRegionDrawable bandwith_icon, money_icon, employees_icon;
    public Array<TextureRegionDrawable> clock_icon;

    public Skin terminal_skin;
    public NinePatchDrawable terminal_patch;
    public NinePatchDrawable win32_patch;

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
        lamp = atlas.findRegion("interior/Lamp");
        desk_1 = atlas.findRegion("interior/Table", 1);
        desk_2 = atlas.findRegion("interior/Table", 2);
        chair = atlas.findRegion("interior/Chair", 1);

        floor_tiles = new Array<TextureRegion>();
        floor_tiles.addAll(atlas.findRegions("ambient/Wood_Floor"));
        gray_character_body = new Array<TextureRegion>();
        hair_01 = new Array<TextureRegion>();
        hair_02 = new Array<TextureRegion>();
        gray_character_body.addAll(character_atlas.findRegions("Char"));
        hair_01.addAll(character_atlas.findRegions("Hair01"));
        hair_02.addAll(character_atlas.findRegions("Hair02"));
        computer = new Array<TextureRegion>(4);
        computer.addAll(atlas.findRegions("interior/Computer_Backfaced"));


        bandwith_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_bandwidth"));
        money_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_money"));
        employees_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_employees"));
        clock_icon = new Array<TextureRegionDrawable>();
        for (TextureRegion t: ui_atlas.findRegions("statusbar_clock")) {
            clock_icon.add(new TextureRegionDrawable(t));
        }

        terminal_skin = new Skin();
        terminal_skin.addRegions(ui_atlas);
        terminal_patch = new NinePatchDrawable(terminal_skin.getPatch("terminal_9_patch"));
        win32_patch = new NinePatchDrawable(terminal_skin.getPatch("popup"));
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

    public Array<TextureRegion> getHairFrames(Employee.HairStyle hairStyle) {
        switch (hairStyle){
            case CRAZY:
                return hair_01;
            case NEAT:
                return hair_02;
            case NERD:
                return hair_01;
            case RASTA:
                return hair_02;
        }
        return hair_01;

    }
}
