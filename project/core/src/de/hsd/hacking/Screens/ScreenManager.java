package de.hsd.hacking.Screens;

import com.badlogic.gdx.Screen;

import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.HackingGame;

/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class ScreenManager {

    private static HackingGame game;
    private static Screen currentScreen;

    public static void initialize(HackingGame myGame){
        game = myGame;
    }

    public static void setMenuScreen(){
        if(currentScreen != null){
            currentScreen.dispose();
        }
        currentScreen = new MenuScreen(game);
        game.setScreen(currentScreen);
    }

    public static void setGameScreen(Boolean resumed){
        if(currentScreen != null){
            currentScreen.dispose();
        }

        if (!resumed)
            initSingletons();

        Team.setInstance(new Team());

        currentScreen = new GameScreen(game);
        game.setScreen(currentScreen);
    }

    public static void disposeCurrentScreen(){
        if (currentScreen != null){
            currentScreen.dispose();
            currentScreen = null;
        }
    }

    public static void setSwipeUpAction(Runnable r) {
        ((GameScreen)currentScreen).setSwipeUpAction(r);
    }

    public static boolean isGameRunning() {
        if (currentScreen.getClass() == GameScreen.class) {
            return true;
        }
        else
            return false;
    }

    private static void initSingletons() {
        GameTime.instance = new GameTime();
        Team.setInstance(new Team());
    }
}
