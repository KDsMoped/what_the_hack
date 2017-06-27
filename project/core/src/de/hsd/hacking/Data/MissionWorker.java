package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.MissionSkillRequirement;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 28.06.2017.
 */

public class MissionWorker implements TimeChangedListener {

    private Mission mission;
    private int remainingMissionDays;
    private List<MissionSkillRequirement> skillRequirements;
    private List<Employee> workers;
    private int lastStep = -1;

    public MissionWorker(Mission mission, List<Employee> employees){
        this.mission = mission;
        this.remainingMissionDays = mission.getDuration();
        skillRequirements = new ArrayList<MissionSkillRequirement>(4);
        workers = employees;
        for (Skill skill:
                mission.getSkill()) {
            skillRequirements.add(new MissionSkillRequirement(skill.getType(), skill.getValue() * mission.getDuration(), 0f));
        }
    }


    @Override
    public void timeChanged(final float time) {
        //time [0-1.0) -> 0-8
        int step = MathUtils.floor(time / (1f / 8));
        if (lastStep == 8) lastStep = -1;
        if (step > lastStep){
            lastStep = step;
            calculateMissionStep();
        }
    }

    /**
     * Gets called every clock step (every ~5s)
     */
    private void calculateMissionStep() {
        for (Employee em :
                workers) {
            for (MissionSkillRequirement req :
                    skillRequirements) {
                int value = em.getSkillValue(req.getSkill());
                workOnSkill(em, req, value);
            }
        }
    }

    private void workOnSkill(Employee em, MissionSkillRequirement req, int value) {
        int dice = MathUtils.random(1, 20);

        if (dice == 1){
            //criticalFailure
            req.incrementCurrentValue(0);
        } else if (dice == 20){
            //criticalSuccess
        }
    }


    @Override
    public void dayChanged(final int days) {
        if (mission.isRunning()){

            if(--remainingMissionDays == 0){
                mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
            }
        }
    }
}
