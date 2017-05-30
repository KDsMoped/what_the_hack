package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;

import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 30.05.17.
 */

public final class SaveGameManager {
    public boolean SaveObject(Object obj) {
        boolean success = false;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(obj);

        if (json != null && json.equals("")) {
            FileHandle file = Gdx.files.local(obj.getClass().getName());
            file.writeString(json, false);

            success = true;
        }

        return success;
    }

    public Object LoadObject(String className) {
        Object obj = null;

        Gson gson = new Gson();
        FileHandle file = Gdx.files.local(className);

        String json = file.readString();

        Class c = TryGetClassFromString(className);

        if (c != null) {
            obj = gson.fromJson(json, c);
        }
        else {
            // Error Handling?
        }

        return obj;
    }

    private Class TryGetClassFromString(String className) {
        Class c = null;

        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            Gdx.app.error(Constants.TAG, "Could not find class for string: " + className);
        }

        return c;
    }
}
