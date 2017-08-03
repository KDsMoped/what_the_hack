package de.hsd.hacking.Entities.Team;

import com.google.protobuf.GeneratedMessageV3;

import java.util.ArrayList;

import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;

import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by domin on 30.05.2017.
 * This class manages Emplyoees and Equipment in ArrayLists;
 */

public class Team {
    private String teamName;

    private ArrayList<Workspace> listOfWorkspaces;
    private Employee selectedEmployee;

    /* Resources */
    public class Resources implements DataContainer {
        private Proto.Resources.Builder data;

        public Resources() {
            data = Proto.Resources.newBuilder();
            data.setMoney(Constants.STARTING_MONEY);
        }

        public Resources(Proto.Resources.Builder proto) {
            data = proto;
            updateResources();
        }

        @Override
        public Proto.Resources getData() {
            return data.build();
        }

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

        public int getSkillBonus(de.hsd.hacking.Entities.Employees.Skill s){
            switch (s.getType().skillType){
                case All_Purpose:   return skill.allPurpose;
                case Software:   return skill.software;
                case Social:   return skill.social;
                case Search:   return skill.search;
                case Network:   return skill.network;
                case Hardware:   return skill.hardware;
                case Crypto:   return skill.crypto;
                default: return 0;
            }
        }

        public int getMoney() {
            return data.getMoney();
        }

        public int getBandwidth() {
            return data.getBandwidth();
        }

        public int getComputationPower() {
            return data.getComputationPower();
        }

        public void setMoney(int money) {
            data.setMoney(money);
        }

        public void setBandwidth(int bandwidth) {
            data.setBandwidth(bandwidth);
        }

        public void setComputationPower(int power) {
            data.setComputationPower(power);
        }
    }
    public final Resources resources;

    private int[] TeamSkills = new int[SkillType.SIZE];

    private static Team instance;

    public Team() {
//        listOfEquipment = new ArrayList<Equipment>();
        listOfWorkspaces = new ArrayList<Workspace>();
        if (SaveGameManager.getResources() == null)
            resources = new Resources();
        else
            resources = new Resources(SaveGameManager.getResources());

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
//    public void addEquipment(Equipment equipment) {
//        listOfEquipment.add(equipment);
//        updateResources();
//    }

//    /* Returns the Equipment object associated with the given index.
//     */
//    public Equipment getEquipment(int index) {
//        return listOfEquipment.get(index);
//    }
//
//    /* Removes the given Equipment from the list.
//     */
//    public void removeEquipment(Equipment e) {
//        listOfEquipment.remove(e);
//        updateResources();
//        //equipment.removeActor(e);
//    }
//
//    /* Removes the Equipment with the given index from the list.
//     */
//    public void removeEquipment(int index) {
//        listOfEquipment.remove(index);
//        Equipment e = getEquipment(index);
//        updateResources();
//        //equipment.removeActor(e);
//    }

//    public ArrayList<Equipment> getEquipmentList() {
//        return listOfEquipment;
//    }


    // Manage Workspaces ///////////////////////////////////////////////////////////////////////////

    public int getWorkspaceCount() {
        return listOfWorkspaces.size();
    }

    public void removeWorkspace(int index) {
        listOfWorkspaces.remove(index);
    }

    // Manage Resources ////////////////////////////////////////////////////////////////////////////

    public int getMoney() { return resources.getMoney(); }

    public void addMoney(int value) {
        resources.setMoney(resources.getMoney() + value);
    }

    public void reduceMoney(int value) {
        resources.setMoney(resources.getMoney() - value);
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

        for(Mission mission : MissionManager.instance().getActiveMissions()){
            newBandwidth -= mission.getUsedBandwidth();
        }

        resources.setBandwidth(newBandwidth);
        resources.setComputationPower(newComputationPower);
        resources.skill.allPurpose = newAllPurpose;
        resources.skill.crypto = newCrypto;
        resources.skill.hardware = newHardware;
        resources.skill.network = newNetwork;
        resources.skill.software = newSoftware;
        resources.skill.search = newSearch;
        resources.skill.social = newSocial;

        //TODO: Add negative outcome if bandwidth is negative
    }


    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(final Employee selectedEmployee1) {
        this.selectedEmployee = selectedEmployee1;
    }

    public boolean isEmployeeSelected() {
        return selectedEmployee != null;
    }

    public void deselectEmployee() {
        if (this.selectedEmployee != null) {
            Employee previouslySelectedEmployee = this.selectedEmployee;
            this.selectedEmployee = null;
            previouslySelectedEmployee.deselect();

        }

    }

    /**
     * Calculates the players game progress based on resources and completed missions.
     * @return
     */
    public int calcGameProgress() {
        float result = 1;

        result += resources.getMoney() * 0.00001f;
//        result += resources.bandwidth * 0.005f;
//        result += resources.computationPower * 0.003f;

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
