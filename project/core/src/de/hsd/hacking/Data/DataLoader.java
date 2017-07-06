package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class DataLoader {

    private static DataLoader INSTANCE;
    private static NameHolder names;
    private static ArrayList<Mission> missions;
    private static CompanyNamesHolder companyNames;

    public static DataLoader getInstance(){
        if (INSTANCE == null){
            INSTANCE = new DataLoader();
        }
        return INSTANCE;
    }

    private DataLoader(){

        FileHandle employeeNames = Gdx.files.internal("data/names.json");

        if (!employeeNames.exists()){
            Gdx.app.log(Constants.TAG, "NAMES JSON FILE DOESNT EXIST");
        }else{
            Json json = new Json();
            names = json.fromJson(NameHolder.class, employeeNames);
        }

        FileHandle m = Gdx.files.internal("data/missions.json");

        if (!m.exists()){
            Gdx.app.log(Constants.TAG, "MISSIONS JSON FILE DOESNT EXIST");
        }else{
            Gson gson = new Gson();
            missions = gson.fromJson(m.reader(), new TypeToken<ArrayList<Mission>>(){}.getType());
        }

        FileHandle compNames = Gdx.files.internal("data/companies.json");

        if (!compNames.exists()){
            Gdx.app.log(Constants.TAG, "COMPANY NAMES JSON FILE DOESNT EXIST");
        }else{
            Json json = new Json();
            companyNames = json.fromJson(CompanyNamesHolder.class, compNames);
        }
    }

    public String[] getNewName(){
        ArrayList<String> surnames = names.getSurNames();
        ArrayList<String> lastNames = names.getLastNames();
        String surName = surnames.get(MathUtils.random(surnames.size() -1));
        String lastName = lastNames.get(MathUtils.random(lastNames.size() -1));

        return new String[]{surName, lastName};
    }

    public Mission getNewMission() {
        return missions.get(MathUtils.random(missions.size() - 1)).Clone();
    }


    public String getNewCompanyName() {
        return companyNames.getRandom();
    }
}
