package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.MissionWorker;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MissionManager implements TimeChangedListener {

    private static final int MAX_ACTIVE_MISSIONS = 4;
    private static final int MAX_OPEN_MISSIONS = 12;
    private static final float REFRESH_RATE = 0.2f;

    private static MissionManager instance;
    private int currentMissionNumber;

    public static MissionManager instance() {

        if (instance == null) return new MissionManager();
        return instance;
    }

    private ArrayList<Mission> activeMissions;
    private ArrayList<Mission> openMissions;
    private ArrayList<Mission> completedMissions;

    private ArrayList<MissionWorker> runningMissions;

    private ArrayList<Callback> refreshMissionListener = new ArrayList<Callback>();

    private MessageManager messageManager;

    private MissionManager() {
        instance = this;

        messageManager = MessageManager.instance();
        currentMissionNumber = 0;
        activeMissions = new ArrayList<Mission>(MAX_ACTIVE_MISSIONS);
        openMissions = new ArrayList<Mission>(MAX_OPEN_MISSIONS);
        completedMissions = new ArrayList<Mission>();
        runningMissions = new ArrayList<MissionWorker>(MAX_ACTIVE_MISSIONS);

        GameTime.instance.addTimeChangedListener(this);

        fillOpenMissions();
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
        int gameProgress = Team.instance().calcGameProgress();

        for (int i = 0; i < MAX_OPEN_MISSIONS; i++) {
            openMissions.add(MissionFactory.CreateRandomMission(gameProgress));
        }
    }

    /**
     * Completes the given mission.
     *
     * @param mission
     */
    public void completeMission(Mission mission) {
        activeMissions.remove(mission);
        completedMissions.add(mission);
//        mission.setOutcome(outcome);

        int money = mission.getOutcome().rewardMoney;
        String text;

        if (mission.hasSuccessText()) text = "Job '" + mission.getName() + "' completed! " + mission.getSuccessText() + " You've earned " + money + "$.";
        else text = "Job '" + mission.getName() + "' completed! You've earned " + money + "$.";

        Team.instance().addMoney(money);
        messageManager.Info(text);
        Gdx.app.log(Constants.TAG, text);

        Team.instance().updateResources();
        notifyRefreshListeners();
    }

    /**
     * Fails the given mission.
     *
     * @param mission
     */
    public void failMission(Mission mission) {
        activeMissions.remove(mission);

        String failText;
        if (mission.hasFailText()) failText = "Job '" + mission.getName() + "' failed. " + mission.getFailText();
        else failText = "Job '" + mission.getName() + "' failed.";

        messageManager.Info(failText);
        Gdx.app.log(Constants.TAG, failText);

        Team.instance().updateResources();
        notifyRefreshListeners();
    }


    /**
     * Aborts the given mission.
     *
     * @param mission
     */
    public void abortMission(Mission mission) {
        activeMissions.remove(mission);
//        completedMissions.add(mission);
//        mission.setOutcome(outcome);

        Team.instance().updateResources();
        notifyRefreshListeners();

        //TODO: Remove MissionWorkers
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

        Team.instance().updateResources();
        notifyRefreshListeners();
        createMissionWorker(mission);
    }

    private void createMissionWorker(Mission mission) {
        int workerNumber = isMissionRunning(mission);
        if (workerNumber < 0) {
            MissionWorker worker = new MissionWorker(mission);
            runningMissions.add(worker);
            GameTime.instance.addTimeChangedListener(worker);
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
     *
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
                return runningMissions.get(workerNumber);
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
        for (int i = 0; i < runningMissions.size(); i++) {
            if (runningMissions.get(i).getMission().getMissionNumber() == mission.getMissionNumber()) {
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
        Gdx.app.log(Constants.TAG, "Your game progress is: " + Team.instance().calcGameProgress());
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

    public void setRunningMissions(ArrayList<MissionWorker> runningMissions) {
        this.runningMissions = runningMissions;
    }
}
