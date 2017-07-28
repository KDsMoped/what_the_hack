package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.protobuf.GeneratedMessageV3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

public final class SaveGameManager {
    static Proto.MessageBar messageBar;

    public static void LoadGame() {
        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/gametime");
            Proto.Global global = Proto.Global.parseFrom(stream);

            Proto.Global.Builder builder = global.toBuilder();
            new GameTime(builder);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, e.getStackTrace().toString());
        }

        try {
            FileInputStream stream = new FileInputStream(Gdx.files.getLocalStoragePath() + "/messagebar");
            messageBar = Proto.MessageBar.parseFrom(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error loading savegame.");
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, e.getStackTrace().toString());
        }
    }

    public static boolean SaveGame() {
        boolean success = false;

        // Game Time
        Proto.Global.Builder gameTime = GameTime.instance.getData();
        Proto.Global gameTimeCompiled = gameTime.build();
        SaveProto(gameTimeCompiled, "gametime");

        SaveGameContainer container = GameStage.instance().getSaveGameContainer();
        Proto.MessageBar messageBarCompiled = container.messageBar.Save();
        SaveProto(messageBarCompiled, "messagebar");

        return success;
    }

    private static void SaveProto(GeneratedMessageV3 message, String filename) {
        try {
            FileOutputStream stream = new FileOutputStream(Gdx.files.getLocalStoragePath() + "/" + filename);
            message.writeTo(stream);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Error while saving " + filename);
            Gdx.app.error(Constants.TAG, e.getMessage());
            Gdx.app.error(Constants.TAG, e.getStackTrace().toString());
        }
    }

    public static boolean SaveObject(Object obj) {
        boolean success = false;

        // We only want to serialize exposed members
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(obj);

        // Save file in the apps local storage
        if (json != null && !json.equals("")) {
            FileHandle file = Gdx.files.local(obj.getClass().getName());
            file.writeString(json, false);

            success = true;
        }

        return success;
    }

    public static Object LoadObject(String className) {
        Object obj = null;

        Gson gson = new Gson();

        // Check weather file exists
        if (Gdx.files.local(className).exists()) {
            FileHandle file = Gdx.files.local(className);

            String json = file.readString();

            Class c = TryGetClassFromString(className);

            if (c != null) {
                // deserialize
                obj = gson.fromJson(json, c);
            }
            else {
                // TBD: UI Error Handling
            }
        }

        return obj;
    }

    private static Class TryGetClassFromString(String className) {
        Class c = null;

        try {
            // Find Class for given class name
            c = Class.forName(className);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Could not find class for string: " + className);
        }

        return c;
    }

    public static Proto.MessageBar.Builder getMessageBar() {
        if (messageBar != null)
            return messageBar.toBuilder();
        else
            return null;
    }
}
