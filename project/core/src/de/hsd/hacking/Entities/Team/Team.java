package de.hsd.hacking.Entities.Team;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeFactory;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.UI.Employee.EmployeeUIElement;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by domin on 30.05.2017.
 * This class manages Emplyoees and Equipment in ArrayLists;
 */

public class Team {
    private String teamName;
    private GameStage stage;

    //private Group employees;
    private Group equipment;
    //private Group workspaces;
    private ArrayList<Equipment> listOfEquipment;
    private ArrayList<Workspace> listOfWorkspaces;
    private Employee selectedEmployee;

    /* Resources */
    public class Resources {
        public int money = Constants.STARTING_MONEY;
        public int bandwidth = 0;
        public int computationPower = 0;

        public class Skill {
            public int allPurpose;
            public int social;
            public int hardware;
            public int software;
            public int network;
            public int crypto;
            public int search;
        }
        public Skill skill = new Skill();
    }
    public Resources resources;

    private int[] TeamSkills = new int[SkillType.SIZE];

    private static Team instance;

    public Team() {
        listOfEquipment = new ArrayList<Equipment>();
        listOfWorkspaces = new ArrayList<Workspace>();
        resources = new Resources();
    }

    public static Team instance() {
        return instance;
    }

    /**
     *Getting and Setting Team Name
     */
    public void setTeamName(String newTeamName) {
        teamName = newTeamName;
    }

    public String getTeamName() {
        return teamName;
    }

    /* Returns the current number of Employees in the Team.
     */

    // Manage Equipment ////////////////////////////////////////////////////////////////////////////

    /* Create Equipment of the specified type and add it to the Team.
     */
/*    public void createAndAddEquipment(Equipment.EquipmentType type) {
        Equipment equipment = EquipmentFactory.getEquipment(type);
        if (equipment != null) {
            listOfEquipment.add(equipment);
        }
        updateResources();
    }
*/

    /* Add given Equipment to the Team.
     */
    public void addEquipment(Equipment equipment) {
        listOfEquipment.add(equipment);
        updateResources();
    }

    /* Returns the Equipment object associated with the given index.
     */
    public Equipment getEquipment(int index) {
        return listOfEquipment.get(index);
    }

    /* Removes the given Equipment from the list.
     */
    public void removeEquipment(Equipment e) {
        listOfEquipment.remove(e);
        updateResources();
        //equipment.removeActor(e);
    }

    /* Removes the Equipment with the given index from the list.
     */
    public void removeEquipment(int index) {
        listOfEquipment.remove(index);
        Equipment e = getEquipment(index);
        updateResources();
        //equipment.removeActor(e);
    }

    public ArrayList<Equipment> getEquipmentList() {
        return listOfEquipment;
    }


    // Manage Workspaces ///////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    public void addWorkspace() {
        Workspace w = new Workspace();
        listOfWorkspaces.add(w);
    }

    public int getWorkspaceCount() {
        return listOfWorkspaces.size();
    }

    public void removeWorkspace(int index) {
        listOfWorkspaces.remove(index);
    }


    // Manage Resources ////////////////////////////////////////////////////////////////////////////
    /*
     void addResource(Equipment.EquipmentAttributeType type, int value) {
        switch(type) {
            case BANDWIDTH:
                resources.bandwidth += value;
            case MONEY:
                resources.money += value;
            case COMPUTATIONPOWER:
                resources.computationPower += value;
        }
    }

    public void reduceResource(Equipment.EquipmentAttributeType type, int value) {
        switch(type) {
            case BANDWIDTH:
                resources.bandwidth -= value;
            case MONEY:
                resources.money -= value;
            case COMPUTATIONPOWER:
                resources.computationPower -= value;
        }
    }

    public void setResource(Equipment.EquipmentAttributeType type, int value) {
        switch(type) {
            case BANDWIDTH:
                resources.bandwidth = value;
            case MONEY:
                resources.money = value;
            case COMPUTATIONPOWER:
                resources.computationPower = value;
        }
    }
    */

    public int getMoney() { return resources.money; }

    public void addMoney(int value) {
        resources.money += value;
    }

    public void reduceMoney(int value) {
        resources.money -= value;
    }

    /**
     * Runs through all the equipment the Team currently holds and updates the resources.
     */
    public void updateResources() {
        int newBandwidth = 0;
        int newComputationPower = 0;
        int newAllPurpose = 0;
        int newSocial = 0;
        int newCrypto = 0;
        int newHardware = 0;
        int newSoftware = 0;
        int newNetwork = 0;
        int newSearch = 0;

        for(Equipment equipment : EquipmentManager.instance().getPurchasedItemList()) {
            newBandwidth += equipment.getBandwidthBonus();
            newComputationPower += equipment.getComputationPowerBonus();
            newAllPurpose += equipment.getAllPurposeSkillBonus();
            newSocial += equipment.getSocialSkillBonus();
            newCrypto += equipment.getCryptoSkillBonus();
            newHardware += equipment.getHardwareSkillBonus();
            newSoftware += equipment.getSoftwareSkillBonus();
            newNetwork += equipment.getNetworkSkillBonus();
            newSearch += equipment.getSearchSkillBonus();
        }

        resources.bandwidth = newBandwidth;
        resources.computationPower = newComputationPower;
        resources.skill.allPurpose = newAllPurpose;
        resources.skill.crypto = newCrypto;
        resources.skill.hardware = newHardware;
        resources.skill.network = newNetwork;
        resources.skill.software = newSoftware;
        resources.skill.search = newSearch;
        resources.skill.social = newSocial;
    }


    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        deselectEmployee();
        this.selectedEmployee = selectedEmployee;
    }

    public boolean isEmployeeSelected() {
        return selectedEmployee != null;
    }

    public void deselectEmployee() {
        if (selectedEmployee != null) {
            Employee deselectedEmployee = selectedEmployee;
            selectedEmployee = null;
            deselectedEmployee.setSelected(false);
        }
    }

    public int calcGameProgress() {
        float result = 1;

        result += resources.money * 0.00001f;
        result += resources.bandwidth * 0.005f;
        result += resources.computationPower * 0.003f;

        for (Mission m : MissionManager.instance().getCompletedMissions()) {
            result += (m.getDifficulty() * (1 + m.getRisk())) * 0.2f;
        }

//        result += MissionManager.instance().getNumberCompletedMissions() * 0.2f;

        return (int) result;
    }

    public static void setInstance(Team instance) {
        Team.instance = instance;
    }
}
