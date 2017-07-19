package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

/**
 * Created by Cuddl3s on 21.05.2017.
 */

public class DataLoader {

    private static DataLoader INSTANCE;
    private static NameHolder names;
    private static ArrayList<Mission> missions;
    private static MissionVariablesHolder missionVariables;

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

        FileHandle variables = Gdx.files.internal("data/missionVariables.json");

        if (!variables.exists()){
            Gdx.app.log(Constants.TAG, "MISSION VARIABLES JSON FILE DOESNT EXIST");
        }else{
            Json json = new Json();
            missionVariables = json.fromJson(MissionVariablesHolder.class, variables);
        }
    }

    public String getNewFullName(Employee.Gender gender){
        String[] name = getNewName(gender);

        return name[0] + " " + name [1];
    }

    public String[] getNewName(Employee.Gender gender){
        ArrayList<String> surnames = names.getSurNames(gender);;
        ArrayList<String> lastNames = names.getLastNames();

        String surName = surnames.get(RandomUtils.randomInt(surnames.size()));
        String lastName = lastNames.get(RandomUtils.randomInt(lastNames.size()));

        return new String[]{surName, lastName};
    }

    /**
     * Gets a mission that meets the given level requirement.
     * @param level
     * @return
     */
    public Mission getNewMission(int level) {

        Mission mission;

        do {
            mission = missions.get(RandomUtils.randomInt(missions.size()));
        }while (mission.getMinLevel() > level || mission.getMaxLevel() < level);


        return mission.Clone();
    }

    public String getNewCompanyName() {
        return missionVariables.getRandomCompany();
    }
    public String getNewInstitution() {
        return missionVariables.getRandomInstitution();
    }
    public String getNewCountryName() {
        return missionVariables.getRandomCountry();
    }
    public String getNewPasswordApplication() {
        return missionVariables.getRandomPasswordApplication();
    }
    public String getNewUniversityName() {
        return missionVariables.getRandomUniversity();
    }
    public String getNewWebServiceName() {
        return missionVariables.getRandomWebService();
    }
    public String getNewSoftwareName() {
        return missionVariables.getRandomSoftware();
    }
    public String getNewTown() {
        return missionVariables.getRandomTown();
    }
}
