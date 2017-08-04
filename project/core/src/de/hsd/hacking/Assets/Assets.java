package de.hsd.hacking.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;

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
    public TextureRegion emoji_twitter;

    public Array<TextureRegion> floor_tiles;
    public Array<TextureRegion> character_1_m;
    public Array<TextureRegion> character_2_m;
    private Array<TextureRegion> character_3_m;
    private Array<TextureRegion> character_4_m;
    public Array<TextureRegion> character_1_f;
    public Array<TextureRegion> character_2_f;
    private Array<TextureRegion> character_3_f;
    private Array<TextureRegion> character_4_f;
    public Array<TextureRegion> character_trump;
    public Array<TextureRegion> char_shadow;
    public Array<TextureRegion> computer;
    public Array<TextureRegion> coffeemachine;
    public Array<TextureRegion> mainmenu_bg;

    public TextureRegionDrawable bandwith_icon, bandwith_icon_black, money_icon, employees_icon;
    public Array<TextureRegionDrawable> clock_icon;
    public Array<TextureRegionDrawable> loading_bar;

    public TextureRegionDrawable ui_icon_exit;
    public TextureRegionDrawable ui_icon_team;
    public TextureRegionDrawable ui_icon_shop;
    public TextureRegionDrawable ui_icon_jobs;
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
    public Sound notificationSound;
    public Sound emojiFailureSound;
    public Sound emojiLevelUpSound;
    public Sound emojiNoSound;
    public Sound emojiOkSound;
    public Sound emojiSpeakingSound;
    public Sound emojiSuccessSound;


    public Assets() {
        instance = this;
        manager = new AssetManager();
    }

    public void load() {

        //manager.load queued assets zum Laden. Das Laden muss aber noch manuell angestoßen werden. Der String Pfad ist später der Key um die Ressource per "manager.get("path")" zu erhalten
        manager.load("img/Game_Assets.atlas", TextureAtlas.class);
        manager.load("img/UI_Assets.atlas", TextureAtlas.class);
        manager.load("img/Character_Assets.atlas", TextureAtlas.class);

        manager.load("sounds/Game_Music.wav", Music.class);
        manager.load("sounds/Menu_Button_Sound.wav", Sound.class);
        manager.load("sounds/UI_Button_Sound.wav", Sound.class);
        manager.load("sounds/Notification_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_Failure_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_LevelUp_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_No_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_Ok_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_Speaking_Sound.wav", Sound.class);
        manager.load("sounds/Emoji_Success_Sound.wav", Sound.class);

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
        gameMusic = manager.get("sounds/Game_Music.wav");
        menuButtonSound = manager.get("sounds/Menu_Button_Sound.wav");
        uiButtonSound = manager.get("sounds/UI_Button_Sound.wav");
        notificationSound = manager.get("sounds/Notification_Sound.wav");
        emojiFailureSound = manager.get("sounds/Emoji_Failure_Sound.wav");
        emojiLevelUpSound = manager.get("sounds/Emoji_LevelUp_Sound.wav");
        emojiNoSound = manager.get("sounds/Emoji_No_Sound.wav");
        emojiOkSound = manager.get("sounds/Emoji_Ok_Sound.wav");
        emojiSpeakingSound = manager.get("sounds/Emoji_Speaking_Sound.wav");
        emojiSuccessSound = manager.get("sounds/Emoji_Success_Sound.wav");

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
        emoji_twitter = character_atlas.findRegion("Emojis/twitter");

        mainmenu_bg = new Array<TextureRegion>();
        mainmenu_bg.addAll(ui_atlas.findRegions("MainMenuBackground"));
//        mainmenu_bg.addAll(atlas.findRegions("ambient/MainMenuBackground"));
        coffeemachine = new Array<TextureRegion>();
        coffeemachine.addAll(atlas.findRegions("interior/CoffeeMachine"));
        floor_tiles = new Array<TextureRegion>();
        floor_tiles.addAll(atlas.findRegions("ambient/Wood_Floor"));
        character_1_m = new Array<TextureRegion>();
        character_2_m = new Array<TextureRegion>();
        character_3_m = new Array<TextureRegion>();
        character_4_m = new Array<TextureRegion>();
        character_1_f = new Array<TextureRegion>();
        character_2_f = new Array<TextureRegion>();
        character_3_f = new Array<TextureRegion>();
        character_4_f = new Array<TextureRegion>();
        character_trump = new Array<TextureRegion>();
        char_shadow = new Array<TextureRegion>();
        character_1_m.addAll(character_atlas.findRegions("Character01_M"));
        character_2_m.addAll(character_atlas.findRegions("Character02_M"));
        character_3_m.addAll(character_atlas.findRegions("Character03_M"));
        character_4_m.addAll(character_atlas.findRegions("Character04_M"));
        character_1_f.addAll(character_atlas.findRegions("Character01_F"));
        character_2_f.addAll(character_atlas.findRegions("Character02_F"));
        character_3_f.addAll(character_atlas.findRegions("Character03_F"));
        character_4_f.addAll(character_atlas.findRegions("Character04_F"));
        character_trump.addAll(character_atlas.findRegions("Specials/Trump"));
        char_shadow.addAll(character_atlas.findRegions("Char_Shadow"));
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

        ui_icon_exit = new TextureRegionDrawable(ui_atlas.findRegion("Icon_Exit"));
        ui_icon_team = new TextureRegionDrawable(ui_atlas.findRegion("Icon_Team"));
        ui_icon_shop = new TextureRegionDrawable(ui_atlas.findRegion("Icon_Shop"));
        ui_icon_jobs = new TextureRegionDrawable(ui_atlas.findRegion("Icon_Jobs"));
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

    public static Animation<TextureRegion> getFrames(float duration, Array<TextureRegion> texture, int from, int to ){
        Array<TextureRegion> animation = new Array<TextureRegion>();

        for (int i = from; i <= to; i++){
            animation.add(texture.get(i));
        }

        return new Animation<TextureRegion>(duration, animation, Animation.PlayMode.LOOP);
    }

    public Array<TextureRegion> getCharBody(Proto.Employee.VisualStyle visualStyle, Proto.Employee.Gender gender, Proto.Employee.HairStyleFemale femaleHairstyle, Proto.Employee.HairStyleMale maleHairstyle) {

        switch (visualStyle) {
            case TRUMP:
                return character_trump;
        }

        switch (gender){
            case MALE:
                return getMaleBody(maleHairstyle);
            case FEMALE:
                return getFemaleBody(femaleHairstyle);
            case UNDECIDED:
                Gdx.app.error("", "Error: There is no face for no gender.");
                return null;
        }
        return null;
    }

    public Array<TextureRegion> getMaleBody(Proto.Employee.HairStyleMale maleHairstyle){
        switch (maleHairstyle) {
           case M_CRAZY:
                return character_4_m; //TODO
            case M_NEAT:
                return character_1_m;
            case M_NERD:
                return character_2_m;
            case M_RASTA:
                return character_3_m; //TODO

            default:
                return character_1_m;
        }
    }

    public Array<TextureRegion> getFemaleBody(Proto.Employee.HairStyleFemale femaleHairstyle){
        switch (femaleHairstyle) {

            case F_CRAZY:
                return character_4_f;
            case F_NEAT:
                return character_1_f;
            case F_NERD:
                return character_2_f;
            case F_RASTA:
                return character_3_f;
            default:
                return character_1_m; //TODO: Add female characters
        }
    }

    public Array<TextureRegion> getCharShadow(Proto.Employee.VisualStyle visualStyle) {
        switch (visualStyle) {
            case TRUMP:
                return char_shadow;
            default:
                return char_shadow;
        }
    }

    public TextureRegion getRandomDesk() {
        boolean desk1 = MathUtils.randomBoolean();
        if (desk1) return desk_1;
        return desk_2;
    }

    public TextureRegionDrawable getSkillIcon(Proto.SkillType skillType) {
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
