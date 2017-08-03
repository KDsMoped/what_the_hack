package de.hsd.hacking;

import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Screens.ScreenManager;

/**
 * This is the central singleton class that controls the game flow and manages instantiation of all other manager classes.
 *
 * @author Hendrik
 */
public class GameManager {
    private static GameManager instance;

    public static GameManager instance() {

        if(instance == null) instance = new GameManager();

        return instance;
    }

    private GameManager() {
    }

    /**
     * Exits the game and dostroys all managers.
     */
    public void exitGame(){
        SaveGameManager.SaveGame();
        ScreenManager.setMenuScreen();
        AudioManager.instance().playUIButtonSound();
        AudioManager.instance().stopMusic();

        //destroy manager instances
        MissionManager.instance().cleanUp();
        MessageManager.instance().cleanUp();
        EquipmentManager.instance().cleanUp();
        EmployeeManager.instance().cleanUp();
        TeamManager.instance().cleanUp();
        GameTime.instance().cleanUp();
    }

    /**
     * Starts a new game and initializes all manager classes.
     */
    public void newGame(){
        AudioManager.instance().playMenuButtonSound();

        createManagerInstances();
        loadManagerDefaultData();

        ScreenManager.setGameScreen(false);
    }

    /**
     * Loads a previous game progress and continues it.
     */
    public void loadGame(){
        AudioManager.instance().playMenuButtonSound();

        SaveGameManager.LoadGame();
        createManagerInstances();
        loadManagerProtoData();

        ScreenManager.setGameScreen(true);
    }

    /**
     * Instantiates all manager classes.
     */
    private void createManagerInstances(){
        //first create all manager instances
        MissionManager.createInstance();
        MessageManager.createInstance();
        EquipmentManager.createInstance();
        EmployeeManager.createInstance();
        TeamManager.createInstance();
        GameTime.createInstance();

        //second initialize references between managers
        MissionManager.instance().initReferences();
        MessageManager.instance().initReferences();
        EquipmentManager.instance().initReferences();
        EmployeeManager.instance().initReferences();
        TeamManager.instance().initReferences();
        GameTime.instance().initReferences();
    }

    /**
     * Loads default states (for new game) of all manager classes.
     */
    private void loadManagerDefaultData(){
        MissionManager.instance().loadDefaultState();
        MessageManager.instance().loadDefaultState();
        EquipmentManager.instance().loadDefaultState();
        EmployeeManager.instance().loadDefaultState();
        TeamManager.instance().loadDefaultState();
        GameTime.instance().loadDefaultState();
    }

    /**
     * Reloads data from previous game run.
     */
    private void loadManagerProtoData(){
        MessageManager.instance().loadState();
        EquipmentManager.instance().loadState();
        EmployeeManager.instance().loadState();
        MissionManager.instance().loadState();
        TeamManager.instance().loadState();
        GameTime.instance().loadState();
    }
}
