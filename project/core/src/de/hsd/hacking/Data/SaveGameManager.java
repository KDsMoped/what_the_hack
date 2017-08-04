package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import com.google.protobuf.GeneratedMessageV3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * This class reads and writes protobuf messages to/from local storage and provides them
 * for the managers such as {@link MissionManager}.
 * @author Julian
 */
public final class SaveGameManager {
    private static Proto.MessageBar messageBar;
    private static Proto.MissionManager missionManager;
    private static Proto.EquipmentManager equipmentManager;
    private static Proto.EmployeeManager employeeManager;
    private static Proto.Resources resources;
    private static Proto.Global gameTime;

    /**
     * Try to load all protobuf messages from local storage.
     */
    public static void LoadGame() {
        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/gametime");

            gameTime = Proto.Global.parseFrom(stream);
        }

        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/resources");
            resources = Proto.Resources.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/messagebar");
            messageBar = Proto.MessageBar.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/equipmentmanager");
            equipmentManager = Proto.EquipmentManager.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/employeemanager");
            employeeManager = Proto.EmployeeManager.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/missionmanager");
            missionManager = Proto.MissionManager.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Save all protobuf messages to local storage.
     */
    public static void SaveGame() {
        // Game Time
        Proto.Global.Builder gameTime = GameTime.instance().getData().toBuilder();
        Proto.Global gameTimeCompiled = gameTime.build();
        SaveProto(gameTimeCompiled, "gametime");

        Proto.Resources resourcesCompiled = TeamManager.instance().resources.getData();
        SaveProto(resourcesCompiled, "resources");

        SaveGameContainer container = GameStage.instance().getSaveGameContainer();
        Proto.MessageBar messageBarCompiled = container.messageBar.Save();
        SaveProto(messageBarCompiled, "messagebar");

        Proto.MissionManager missionManagerCompiled = MissionManager.instance().Save();
        SaveProto(missionManagerCompiled, "missionmanager");

        Proto.EquipmentManager equipmentManagerCompiled = EquipmentManager.instance().Save();
        SaveProto(equipmentManagerCompiled, "equipmentmanager");

        Proto.EmployeeManager employeeManagerCompiled = EmployeeManager.instance().Save();
        SaveProto(employeeManagerCompiled, "employeemanager");
    }

    private static void SaveProto(GeneratedMessageV3 message, String filename) {
        try {
            FileOutputStream stream = new FileOutputStream(Gdx.files.getLocalStoragePath() + "/" + filename);
            message.writeTo(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error while saving " + filename);
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, Arrays.toString(e.getStackTrace()));
        }
    }

    public static Proto.MessageBar.Builder getMessageBar() {
        if (messageBar != null)
            return messageBar.toBuilder();
        else
            return null;
    }

    public static Proto.MissionManager.Builder getMissionManager() {
        if (missionManager != null)
            return missionManager.toBuilder();
        else
            return null;
    }

    public static Proto.EquipmentManager.Builder getEquipmentManager() {
        if (equipmentManager != null)
            return equipmentManager.toBuilder();
        else
            return null;
    }

    public static Proto.EmployeeManager.Builder getEmployeeManager() {
        if (employeeManager != null)
            return employeeManager.toBuilder();
        else
            return null;
    }

    public static Proto.Resources.Builder getResources() {
        if (resources != null)
            return resources.toBuilder();
        else
            return null;
    }

    public static Proto.Global.Builder getGameTime() {
        if (gameTime != null)
            return gameTime.toBuilder();
        else
            return null;
    }
}
