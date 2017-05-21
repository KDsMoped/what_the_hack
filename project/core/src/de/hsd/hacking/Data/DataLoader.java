package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class DataLoader {

    private static DataLoader INSTANCE;
    private static NameHolder names;


    public static DataLoader getInstance(){
        if (INSTANCE == null){
            INSTANCE = new DataLoader();
        }
        return INSTANCE;
    }

    private DataLoader(){

        FileHandle employeeNames = Gdx.files.local("data/names.json");
        if (!employeeNames.exists()){
            Gdx.app.log(Constants.TAG, "NAMES JSON FILE DOESNT EXIST");
        }else{
            Json json = new Json();
            names = json.fromJson(NameHolder.class, employeeNames);
        }
    }

    public String[] getNewName(){
        ArrayList<String> surnames = names.getSurNames();
        ArrayList<String> lastNames = names.getLastNames();
        String surName = surnames.get(MathUtils.random(surnames.size() -1));
        String lastName = lastNames.get(MathUtils.random(lastNames.size() -1));

        return new String[]{surName, lastName};
    }
}
