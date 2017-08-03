package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Proto;

public class Assets {

    private static Assets instance;


    public static Assets instance() {
        return instance;
    }

    public AssetManager manager;

    public static BitmapFont standard_font;
    public BitmapFont header_font;
    public BitmapFont gold_font;
    public static BitmapFont gold_font_small;
    public BitmapFont status_bar_font;
    public BitmapFont tiny_label_font;

    private TextureAtlas atlas;
    public TextureAtlas ui_atlas;
    private TextureAtlas character_atlas;

    public TextureRegion room_bg;
    public TextureRegion room_fg;
    public TextureRegion lamp;
    public TextureRegion desk_1;
    public TextureRegion desk_2;
    public TextureRegion desk_bf_1;
    public TextureRegion desk_bf_2;
    public TextureRegion chair;
    public TextureRegion emoji_success;
    public TextureRegion emoji_angry;
    public TextureRegion emoji_ok;
    public TextureRegion emoji_no;
    public TextureRegion emoji_levelup;

    public Array<TextureRegion> floor_tiles;
    public Array<TextureRegion> gray_character_body;
    public Array<TextureRegion> hair_01;
    public Array<TextureRegion> hair_02;
    public Array<TextureRegion> computer;
    public Array<TextureRegion> coffeemachine;
    public Array<TextureRegion> mainmenu_bg;

    public TextureRegionDrawable bandwith_icon, bandwith_icon_black, money_icon, employees_icon;
    public Array<TextureRegionDrawable> clock_icon;
    public Array<TextureRegionDrawable> loading_bar;

    public TextureRegionDrawable ui_calendar;
    public TextureRegionDrawable skill_icon_social;
    public TextureRegionDrawable skill_icon_software;
    public TextureRegionDrawable skill_icon_hardware;
    public TextureRegionDrawable skill_icon_crypto;
    public TextureRegionDrawable skill_icon_search;
    public TextureRegionDrawable skill_icon_network;
    public TextureRegionDrawable skill_icon_allpurpose;

    // Assets for Notification Bar
    public TextureRegionDrawable ui_up_arrow_inverted;
    public TextureRegionDrawable ui_info, ui_warning, ui_error, ui_help;

    public Skin terminal_skin;
    public NinePatchDrawable terminal_patch;
    public NinePatchDrawable win32_patch;
    public NinePatchDrawable table_border_patch, tab_view_border_patch;

    public NinePatchDrawable table_dimm_patch;

    // Equipment Icons
    public TextureRegionDrawable computer_icon;
    public TextureRegionDrawable router_icon;
    public TextureRegionDrawable coffeemachine_icon;

    // Audio Assets
    public Music gameMusic;

    public Sound menuButtonSound;
    public Sound uiButtonSound;


    public Assets() {
        instance = this;
        manager = new AssetManager();
    }

    public void load() {

        //manager.load queued assets zum Laden. Das Laden muss aber noch manuell angestoßen werden. Der String Pfad ist später der Key um die Ressource per "manager.get("path")" zu erhalten
        manager.load("img/Game_Assets.atlas", TextureAtlas.class);
        manager.load("img/UI_Assets.atlas", TextureAtlas.class);
        manager.load("img/Character_Assets.atlas", TextureAtlas.class);

        manager.load("sounds/Retro_Game_Sounds_SFX_28_16bit.wav", Sound.class);
        manager.load("sounds/button_on_off_064_16bit.wav", Sound.class);
        manager.load("sounds/Background_Music_16bit.wav", Music.class);

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

        // Sounds
        gameMusic = manager.get("sounds/Background_Music_16bit.wav");
        menuButtonSound = manager.get("sounds/Retro_Game_Sounds_SFX_28_16bit.wav");
        uiButtonSound = manager.get("sounds/button_on_off_064_16bit.wav");

        //BitmapFonts müssen per "Hiero" Tool erzeugt werden. Das Tool findet ihr auf der libgdx Seite.
        standard_font = new BitmapFont(Gdx.files.internal("fonts/test_font.fnt"), Gdx.files.internal("fonts/test_font.png"), false);
        tiny_label_font = new BitmapFont(Gdx.files.internal("fonts/status_bar_font.fnt"), Gdx.files.internal("fonts/status_bar_font.png"), false);
        header_font = new BitmapFont(Gdx.files.internal("fonts/test_font_big_white.fnt"), Gdx.files.internal("fonts/test_font_big_white.png"), false);
        status_bar_font = new BitmapFont(Gdx.files.internal("fonts/status_bar_font.fnt"), Gdx.files.internal("fonts/status_bar_font.png"), false);
        //tiny_label_font = new BitmapFont(Gdx.files.internal("fonts/status_bar_font.fnt"), Gdx.files.internal("fonts/status_bar_font.png"), false);
        //...//

        room_bg = atlas.findRegion("ambient/Room_Background");
        room_fg = atlas.findRegion("ambient/Room_FrontWall");
        lamp = atlas.findRegion("interior/Lamp");
        desk_1 = atlas.findRegion("interior/Table", 1);
        desk_2 = atlas.findRegion("interior/Table", 2);
        desk_bf_1 = atlas.findRegion("interior/Table_bf", 1);
        desk_bf_2 = atlas.findRegion("interior/Table_bf", 2);
        chair = atlas.findRegion("interior/Chair", 1);
        emoji_success = character_atlas.findRegion("Emojis/success");
        emoji_angry = character_atlas.findRegion("Emojis/angry");
        emoji_ok = character_atlas.findRegion("Emojis/ok");
        emoji_no = character_atlas.findRegion("Emojis/no");
        emoji_levelup = character_atlas.findRegion("Emojis/levelup");

        mainmenu_bg = new Array<TextureRegion>();
        mainmenu_bg.addAll(ui_atlas.findRegions("MainMenuBackground"));
//        mainmenu_bg.addAll(atlas.findRegions("ambient/MainMenuBackground"));
        coffeemachine = new Array<TextureRegion>();
        coffeemachine.addAll(atlas.findRegions("interior/CoffeeMachine"));
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
        bandwith_icon_black = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_bandwidth_black"));
        money_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_money"));
        employees_icon = new TextureRegionDrawable(ui_atlas.findRegion("statusbar_employees"));
        clock_icon = new Array<TextureRegionDrawable>();
        for (TextureRegion t : ui_atlas.findRegions("statusbar_clock")) {
            clock_icon.add(new TextureRegionDrawable(t));
        }

        ui_calendar = new TextureRegionDrawable(ui_atlas.findRegion("calendar"));
        ui_up_arrow_inverted = new TextureRegionDrawable(ui_atlas.findRegion("up_arrow_inverted"));

        ui_info = new TextureRegionDrawable(ui_atlas.findRegion("info"));
        ui_error = new TextureRegionDrawable(ui_atlas.findRegion("error"));
        ui_help = new TextureRegionDrawable(ui_atlas.findRegion("help"));
        ui_warning = new TextureRegionDrawable(ui_atlas.findRegion("warning"));

        skill_icon_social = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 0));
        skill_icon_crypto = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 1));
        skill_icon_software = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 2));
        skill_icon_hardware = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 3));
        skill_icon_network = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 4));
        skill_icon_search = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 5));
        skill_icon_allpurpose = new TextureRegionDrawable(ui_atlas.findRegion("Skill_Icons", 6));

        loading_bar = new Array<TextureRegionDrawable>(4);
        for (TextureRegion t : ui_atlas.findRegions("Loading_Bar_Normal")) {
            loading_bar.add(new TextureRegionDrawable(t));
        }

        terminal_skin = new Skin();
        terminal_skin.addRegions(ui_atlas);
        terminal_patch = new NinePatchDrawable(terminal_skin.getPatch("terminal_9_patch"));
        win32_patch = new NinePatchDrawable(terminal_skin.getPatch("popup"));
        table_border_patch = new NinePatchDrawable(terminal_skin.getPatch("table_border"));
        tab_view_border_patch = new NinePatchDrawable(terminal_skin.getPatch("tab_view_border"));
        table_dimm_patch = new NinePatchDrawable(terminal_skin.getPatch("dimm"));

        coffeemachine_icon = new TextureRegionDrawable(atlas.findRegion("icon/CoffeeMachine", 3));
        computer_icon = new TextureRegionDrawable(atlas.findRegion("icon/Computer_Backfaced", 3));
        router_icon = new TextureRegionDrawable(atlas.findRegion("icon/Router", 1));
    }

    /*Muss von außerhalb aufgerufen werden wenn manager.update() true zurück gibt*/
    public void loadingDone() {

    }

    public void dispose() {
        character_atlas.dispose();
        ui_atlas.dispose();
        atlas.dispose();
        manager.clear();

    }

    public Array<TextureRegion> getHairFrames(Proto.Employee.HairStyle hairStyle) {
        switch (hairStyle) {
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

    public TextureRegion getRandomDesk() {
        boolean desk1 = MathUtils.randomBoolean();
        if (desk1) return desk_1;
        return desk_2;
    }

    public TextureRegionDrawable getSkillIcon(Proto.Skill.SkillType skillType) {
        switch (skillType) {
            case Social:
                return  skill_icon_social;
            case Hardware:
                return skill_icon_hardware;
            case Software:
                return skill_icon_software;
            case Network:
                return skill_icon_network;
            case Crypto:
                return skill_icon_crypto;
            case Search:
                return skill_icon_search;
            case All_Purpose:
                return skill_icon_allpurpose;
            default:
                return skill_icon_software;
        }
    }
}
