package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Utils.Constants;

public final class SaveGameManager {
    @Expose private static SaveGameContainer savedObjects = new SaveGameContainer() {
    };

    // TBD
    public static void LoadGame() {
        savedObjects = (SaveGameContainer) LoadObject("de.hsd.hacking.Data.SaveGameContainer");

        GameTime.instance = savedObjects.getGameTime();
    }

    // TBD
    public static boolean SaveGame() {
        boolean success = false;
        savedObjects = new SaveGameContainer();

        // Employee
        // Employee Status
        // Employee Stats
        //
        // Game Time

        savedObjects.setGameTime(GameTime.instance);
        // Messages

        SaveObject(savedObjects);

        return success;
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
}
