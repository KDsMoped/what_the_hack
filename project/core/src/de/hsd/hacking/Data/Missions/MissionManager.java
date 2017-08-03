package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Data.*;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class is used to manage the missions in the game.
 *
 * @author Hendrik
 */
public class MissionManager implements Manager, TimeChangedListener, ProtobufHandler {
    private static final int MAX_ACTIVE_MISSIONS = 4;
    private static final int MAX_OPEN_MISSIONS = 6;
    private static final float REFRESH_RATE = 0.3f;

    private static MissionManager instance;
    private int currentMissionNumber;

    private ArrayList<Mission> activeMissions;
    private ArrayList<Mission> openMissions;
    private ArrayList<Mission> completedMissions;
    private ArrayList<MissionWorker> runningMissionWorkers;
    private ArrayList<Callback> refreshMissionListener = new ArrayList<Callback>();

    private MessageManager messageManager;

    /**
     * Creates an instance of this manager.
     */
    public static void createInstance() {
        if (instance != null){
            Gdx.app.error("", "ERROR: Instance of MissionManager has not been destroyed before creating new one.");
            return;
        }

        instance = new MissionManager();
    }

    /**
     * Provides an instance of this manager;
     * @return
     */
    public static MissionManager instance() {

        if (instance == null)
            Gdx.app.error("", "ERROR: Instance of MissionManager has not been created yet. Use createInstance() to do so.");

        return instance;
    }

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    @Override
    public void initReferences() {
        messageManager = MessageManager.instance();
        GameTime.instance().addTimeChangedListener(this);
    }

    /**
     * Creates the default state of this manager when a new game is started.
     */
    @Override
    public void loadDefaultState() {
        fillOpenMissions();
    }

    /**
     * Recreates the state this manager had before serialization.
     */
    @Override
    public void loadState() {
        Proto.MissionManager.Builder proto = SaveGameManager.getMissionManager();
        currentMissionNumber = proto.getCurrentMissionNumber();

        for (Proto.Mission mission : proto.getOpenMissionsList()) {
            openMissions.add(new Mission(mission.toBuilder()));
        }

        for (Proto.Mission mission : proto.getActiveMissionsList()) {
            activeMissions.add(new Mission(mission.toBuilder()));
        }

        for (Proto.Mission mission : proto.getCompletedMissionsList()) {
            completedMissions.add(new Mission(mission.toBuilder()));
        }

        for (Proto.MissionWorker worker : proto.getWorkersList()) {
            runningMissionWorkers.add(new MissionWorker(worker.toBuilder()));
        }
    }

    /**
     * Destroys manager this instance.
     */
    @Override
    public void cleanUp() {
        instance = null;
    }

    private MissionManager() {
        currentMissionNumber = 0;
        activeMissions = new ArrayList<Mission>(MAX_ACTIVE_MISSIONS);
        openMissions = new ArrayList<Mission>(MAX_OPEN_MISSIONS);
        completedMissions = new ArrayList<Mission>();
        runningMissionWorkers = new ArrayList<MissionWorker>(MAX_ACTIVE_MISSIONS);
    }

    /**
     * Refreshes the available open missions and notifies all listeners.
     */
    private void refreshOpenMissions() {

        removeMissions(REFRESH_RATE);
        fillOpenMissions();

        notifyRefreshListeners();
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshMissionListener.toArray(new Callback[refreshMissionListener.size()])) {
            c.callback();
        }
    }

    /**
     * Removes the given fraction of open missions.
     *
     * @param fraction
     */
    private void removeMissions(float fraction) {

        int removeAmount = MathUtils.clamp((int) (fraction * MAX_OPEN_MISSIONS), 0, openMissions.size());

        for (int i = 0; i < removeAmount; i++) {
            openMissions.remove(0);
        }
    }

    /**
     * Creates open missions until the maximum is reached.
     */
    private void fillOpenMissions() {
        int gameProgress = TeamManager.instance().calcGameProgress();

        for (int i = openMissions.size(); i < MAX_OPEN_MISSIONS; i++) {
            openMissions.add(MissionFactory.CreateRandomMission(gameProgress));
        }
    }

    /**
     * Completes the given mission.
     *
     * @param mission1
     */
    public void completeMission(final Mission mission1) {
        if (activeMissions.remove(mission1)) {
            completedMissions.add(mission1);
            messageManager.Info("Job " + mission1.getName() + ": " + mission1.getSuccessText());
            Gdx.app.log(Constants.TAG, "Job " + mission1.getName() + ": " + mission1.getSuccessText());

            int money = mission1.getRewardMoney();
            String text;

            if (mission1.hasSuccessText())
                text = "Job '" + mission1.getName() + "' completed! " + mission1.getSuccessText() + " You've earned " + money + "$.";
            else text = "Job '" + mission1.getName() + "' completed! You've earned " + money + "$.";

            TeamManager.instance().addMoney(money);
            messageManager.Info(text);
            Gdx.app.log(Constants.TAG, text);

            TeamManager.instance().updateResources();
            notifyRefreshListeners();
        }
//        mission.setOutcome(outcome);
    }

    /**
     * Fails the given mission.
     *
     * @param mission1
     */
    public void failMission(final Mission mission1) {
        if (activeMissions.remove(mission1)) {
            String failText;
            if (mission1.hasFailText()) failText = "Job '" + mission1.getName() + "' failed. " + mission1.getFailText();
            else failText = "Job '" + mission1.getName() + "' failed.";

            messageManager.Info(failText);
            Gdx.app.log(Constants.TAG, failText);

            TeamManager.instance().updateResources();
            notifyRefreshListeners();
        }
    }


    /**
     * Aborts the given mission.
     *
     * @param mission1
     */
    public void abortMission(Mission mission1) {
        activeMissions.remove(mission1);
        int i = isMissionRunning(mission1);
        if (i >= 0) {
            MissionWorker m = runningMissionWorkers.get(i);
            GameTime.instance().removeTimeChangedListener(m);
            mission1.Abort();
            runningMissionWorkers.remove(m);
        }

        TeamManager.instance().updateResources();
        notifyRefreshListeners();
    }

    /**
     * Starts the given mission.
     *
     * @param mission
     */
    public void startMission(Mission mission) {

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

        TeamManager.instance().updateResources();
        notifyRefreshListeners();
        createMissionWorker(mission);
    }

    private void createMissionWorker(Mission mission) {
        int workerNumber = isMissionRunning(mission);
        if (workerNumber < 0) {
            MissionWorker worker = new MissionWorker(mission);
            runningMissionWorkers.add(worker);
            GameTime.instance().addTimeChangedListener(worker);
        }
    }

    /**
     * Adds the employee to an existing {@link MissionWorker} or creates a new one.
     *
     * @param employee The employee to add to the MissionWorker
     * @return the MissionWorker for that mission
     */
    public MissionWorker startWorking(final Employee employee) {
        if (employee.getCurrentMission() == null) return null;
        int workerNumber = isMissionRunning(employee.getCurrentMission());
        if (workerNumber > -1) {
            MissionWorker worker = runningMissionWorkers.get(workerNumber);
            worker.addEmployee(employee);
            return worker;
        } else {
            if (activeMissions.contains(employee.getCurrentMission())) {
                MissionWorker worker = new MissionWorker(employee.getCurrentMission());
                worker.addEmployee(employee);
                runningMissionWorkers.add(worker);
                GameTime.instance().addTimeChangedListener(worker);
                return worker;
            } else {
                Gdx.app.error(Constants.TAG, "Mission that was not active was chosen to be worked on.");
                return null;
            }
        }
    }

    /**
     * Removes the employee from the corresponding {@link MissionWorker}
     *
     * @param employee Employee to remove
     */
    public void stopWorking(final Employee employee) {
        if (employee.getCurrentMission() != null) {
            int workerNumber = isMissionRunning(employee.getCurrentMission());
            if (workerNumber > -1) {
                MissionWorker worker = runningMissionWorkers.get(workerNumber);
                if (worker.removeEmployee(employee)) {
                    employee.setCurrentMission(null);
                }
                //Remove MissionWorker instance if no longer needed, and add to completed missions;
                if (worker.hasNoWorkers() && worker.getMission().isFinished()) {
                    runningMissionWorkers.remove(worker);
                    GameTime.instance().removeTimeChangedListener(worker);

                    if (worker.getMission().isCompleted()) {
                        completeMission(worker.getMission());
                    } else {
                        failMission(worker.getMission());
                    }
                }
            }
        }
    }

    /**
     * Gets the {@link MissionWorker} for a given working Employee.
     * Returns null if Employee is not working on mission.
     *
     * @param employee
     * @return
     */
    public MissionWorker getMissionWorker(final Employee employee) {
        if (employee.getCurrentMission() != null) {
            int workerNumber = isMissionRunning(employee.getCurrentMission());
            if (workerNumber > -1) {
                return runningMissionWorkers.get(workerNumber);
            }
        }
        return null;
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

    private int isMissionRunning(Mission mission) {
        for (int i = 0; i < runningMissionWorkers.size(); i++) {
            if (runningMissionWorkers.get(i).getMission().getMissionNumber() == mission.getMissionNumber()) {
                return i;
            }
        }
        return -1;
    }

    public int getNumberCompletedMissions() {
        return completedMissions.size();
    }

    @Override
    public void timeChanged(float time) {
    }

    @Override
    public void timeStepChanged(int step) {
    }

    @Override
    public void dayChanged(int days) {
//        refreshOpenMissions(); //Too fast
    }

    @Override
    public void weekChanged(int week) {
        //TODO: Tell user about game progress
        Gdx.app.log(Constants.TAG, "Your game progress is: " + TeamManager.instance().calcGameProgress());
        refreshOpenMissions();
    }

    public void addRefreshMissionListener(Callback callback) {
        if (!refreshMissionListener.contains(callback)) refreshMissionListener.add(callback);
    }

    public void setCurrentMissionNumber(int currentMissionNumber) {
        this.currentMissionNumber = currentMissionNumber;
    }

    public void setActiveMissions(ArrayList<Mission> activeMissions) {
        this.activeMissions = activeMissions;
    }

    public void setOpenMissions(ArrayList<Mission> openMissions) {
        this.openMissions = openMissions;
    }

    public void setCompletedMissions(ArrayList<Mission> completedMissions) {
        this.completedMissions = completedMissions;
    }

    public void setRunningMissionWorkers(ArrayList<MissionWorker> runningMissionWorkers) {
        this.runningMissionWorkers = runningMissionWorkers;
    }

    public int getActiveMissionId(Mission mission) {
        return activeMissions.indexOf(mission);
    }

    public Mission getActiveMission(int id) {
        return activeMissions.get(id);
    }

    /**
     * Save all the current missions
     *
     * @return Build Proto MissionManager object to write to disk.
     */
    public Proto.MissionManager Save() {
        Proto.MissionManager.Builder builder = Proto.MissionManager.newBuilder();

        builder.setCurrentMissionNumber(currentMissionNumber);

        for (Mission mission : activeMissions) {
            builder.addActiveMissions(mission.getData());
        }

        for (Mission mission : completedMissions) {
            builder.addCompletedMissions(mission.getData());
        }

        for (Mission mission : openMissions) {
            builder.addOpenMissions(mission.getData());
        }

        for (MissionWorker worker : runningMissionWorkers) {
            builder.addWorkers(worker.getData());
        }

        return builder.build();
    }

    /**
     * Restores the missions from a previous game.
     *
     * @return True if missions where loaded.
     */
    public Boolean Load() {
        Proto.MissionManager.Builder proto = SaveGameManager.getMissionManager();
        if (proto != null) {
            currentMissionNumber = proto.getCurrentMissionNumber();

            for (Proto.Mission mission : proto.getOpenMissionsList()) {
                openMissions.add(new Mission(mission.toBuilder()));
            }

            for (Proto.Mission mission : proto.getActiveMissionsList()) {
                activeMissions.add(new Mission(mission.toBuilder()));
            }

            for (Proto.Mission mission : proto.getCompletedMissionsList()) {
                completedMissions.add(new Mission(mission.toBuilder()));
            }

            for (Proto.MissionWorker worker : proto.getWorkersList()) {
                runningMissionWorkers.add(new MissionWorker(worker.toBuilder()));
            }

            return true;
        }
        return false;
    }
}
