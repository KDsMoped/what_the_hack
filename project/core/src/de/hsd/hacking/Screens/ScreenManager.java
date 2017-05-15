package de.hsd.hacking.Screens;

import com.badlogic.gdx.Screen;

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

    public static void disposeCurrentScreen(){
        if (currentScreen != null){
            currentScreen.dispose();
            currentScreen = null;
        }
    }

}
