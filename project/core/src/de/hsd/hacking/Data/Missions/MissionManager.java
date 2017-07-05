package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MissionManager {

    private static final int MAX_ACTIVE_MISSIONS = 4;
    private static final int MAX_OPEN_MISSIONS = 12;
    private static final float REFRESH_RATE = 0.3f;

    private static MissionManager instance;
    private int currentMissionNumber;

    public static MissionManager instance() {

        if(instance == null) return new MissionManager();
        return instance;
    }

    private ArrayList<Mission> activeMissions;
    private ArrayList<Mission> openMissions;
    private ArrayList<Mission> completedMissions;

    private ArrayList<MissionWorker> runningMissions;

//    private int numSuccessfulMissions;

    private MissionManager() {
        instance = this;

        currentMissionNumber = 0;
        activeMissions = new ArrayList<Mission>(MAX_ACTIVE_MISSIONS);
        openMissions = new ArrayList<Mission>(MAX_OPEN_MISSIONS);
        completedMissions = new ArrayList<Mission>();
        runningMissions = new ArrayList<MissionWorker>(MAX_ACTIVE_MISSIONS);

        fillOpenMissions();

        Gdx.app.log(Constants.TAG, "open missions: " + openMissions.size());
        startMission(openMissions.get(0));
        Gdx.app.log(Constants.TAG, "open missions: " + openMissions.size());
        startMission(openMissions.get(0));
        Gdx.app.log(Constants.TAG, "open missions: " + openMissions.size());
        startMission(openMissions.get(0));
    }

    private void refreshOpenMissions(){

        removeMissions(REFRESH_RATE);
        fillOpenMissions();
    }

    private void removeMissions(float fraction){

        int removeAmount = MathUtils.clamp((int) (fraction * MAX_OPEN_MISSIONS), 0, openMissions.size());

        for(int i = 0; i < removeAmount; i++){
            openMissions.remove(0);
        }
    }

    private void fillOpenMissions(){
        int gameProgress = Team.instance().calcGameProgress();

        for (int i = 0; i < MAX_OPEN_MISSIONS; i++){
            openMissions.add(MissionFactory.CreateRandomMission(gameProgress));
        }
    }

    /**
     * Completes this mission
     * @param mission
     * @param outcome
     */
    public void completeMission(Mission mission, MissionOutcome outcome){
        activeMissions.remove(mission);
        completedMissions.add(mission);
        mission.setOutcome(outcome);
    }

    /**
     * Starts the given mission.
     * @param mission
     */
    public void startMission(Mission mission){

        if (activeMissions.contains(mission)) {
            Gdx.app.error(Constants.TAG, "Error: This mission is already active!");
            return;
        }
        if (activeMissions.size() >= MAX_ACTIVE_MISSIONS) {
            Gdx.app.error(Constants.TAG, "Error: Exceeding max number of active missions!");
            return;
        }
        mission.setMissionNumber(currentMissionNumber++);
        openMissions.remove(mission);
        activeMissions.add(mission);
        mission.Start();
    }

    /**
     * Adds the employee to an existing {@link MissionWorker} or creates a new one.
     * @param employee The employee to add to the MissionWorker
     * @return the MissionWorker for that mission
     */
    public MissionWorker startWorking(final Employee employee) {
        if (employee.getCurrentMission() == null) return null;
        int workerNumber = isMissionRunning(employee.getCurrentMission());
        if (workerNumber > -1) {
            MissionWorker worker = runningMissions.get(workerNumber);
            worker.addEmployee(employee);
            return worker;
        } else {
            if (activeMissions.contains(employee.getCurrentMission())) {
                MissionWorker worker = new MissionWorker(employee.getCurrentMission());
                worker.addEmployee(employee);
                runningMissions.add(worker);
                GameTime.instance.addTimeChangedListener(worker);
                return worker;
            } else {
                Gdx.app.error(Constants.TAG, "Mission that was not active was chosen to be worked on.");
                return null;
            }
        }
    }

    /**
     * Removes the employee from the corresponding {@link MissionWorker}
     * @param employee Employee to remove
     */
    public void stopWorking(Employee employee) {
        if (employee.getCurrentMission() != null) {
            int workerNumber = isMissionRunning(employee.getCurrentMission());
            if (workerNumber > -1) {
                MissionWorker worker = runningMissions.get(workerNumber);
                if (worker.removeEmployee(employee)) {
                    employee.setCurrentMission(null);
                }
                //Remove MissionWorker instance if no longer needed, and add to completed missions;
                if (worker.hasNoWorkers() && worker.getMission().isFinished()) {
                    runningMissions.remove(worker);
                    GameTime.instance.removeTimeChangedListener(worker);
                    activeMissions.remove(worker.getMission());
                    if (worker.getMission().isCompleted()) {
                        completedMissions.add(worker.getMission());
                    }
                }
            }
        }
    }

    public Collection<Mission> getActiveMissions() {
        return Collections.unmodifiableCollection(activeMissions);
    }
    public Collection<Mission> getOpenMissions() {
        return Collections.unmodifiableCollection(openMissions);
    }
    public Collection<Mission> getCompletedMissions() {
        return Collections.unmodifiableCollection(completedMissions);
    }

    private int isMissionRunning(Mission mission){
        for (int i = 0; i < runningMissions.size(); i++) {
            if (runningMissions.get(i).getMission().getMissionNumber() == mission.getMissionNumber()){
                return i;
            }
        }
        return -1;
    }

    public int getNumberCompletedMissions(){
        return completedMissions.size();
    }
}
