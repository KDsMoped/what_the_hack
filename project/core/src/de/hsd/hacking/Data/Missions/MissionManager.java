package de.hsd.hacking.Data.Missions;

import com.badlogic.gdx.math.MathUtils;
import de.hsd.hacking.Entities.Team.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MissionManager {

    private static final int MAX_ACTIVE_MISSIONS = 4;
    private static final int MAX_OPEN_MISSIONS = 12;
    private static final float REFRESH_RATE = 0.3f;

    private static MissionManager instance;

    public static MissionManager getInstance() {

        if(instance == null) return new MissionManager();
        return instance;
    }

    private ArrayList<Mission> activeMissions;
    private ArrayList<Mission> openMissions;
    private ArrayList<Mission> completedMissions;

    public MissionManager() {
        instance = this;

        activeMissions = new ArrayList<Mission>();
        openMissions = new ArrayList<Mission>();
        completedMissions = new ArrayList<Mission>();

        FillOpenMissions();
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

    public void RefreshOpenMissions(){

        RemoveMissions(REFRESH_RATE);
        FillOpenMissions();
    }

    private void RemoveMissions(float fraction){

        int removeAmount = MathUtils.clamp((int) (fraction * MAX_OPEN_MISSIONS), 0, openMissions.size());

        for(int i = 0; i < removeAmount; i++){
            openMissions.remove(0);
        }
    }

    public void FillOpenMissions(){
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
    public void CompleteMission(Mission mission, MissionOutcome outcome){
        activeMissions.remove(mission);
        completedMissions.add(mission);
        mission.setOutcome(outcome);
    }

    /**
     * Starts the given mission.
     * @param mission
     */
    public void StartMission(Mission mission){

        openMissions.remove(mission);
        activeMissions.add(mission);
        mission.Start();
    }
}
