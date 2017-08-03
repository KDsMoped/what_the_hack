package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Entities.Employees.*;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

/**
 * Class that handles employees working on a particular mission
 * @author Florian
 */

public class MissionWorker implements TimeChangedListener {

    private static final int DICE_SIDES = 40;
    private Mission mission;
    private int remainingMissionDays;
    private List<MissionSkillRequirement> skillRequirements;
    private List<Employee> employees;

    public MissionWorker(final Mission mission1) {
        this.mission = mission1;
        this.remainingMissionDays = mission.getDuration();
        skillRequirements = new ArrayList<MissionSkillRequirement>(4);
        employees = new ArrayList<Employee>(4);
        for (Skill skill
                : mission.getSkill()) {
            skillRequirements.add(new MissionSkillRequirement(skill.getType(), skill.getValue() * MathUtils.clamp(mission.getDuration() / 2f, 1f, 99f), 0f));
        }
    }

    public Mission getMission() {
        return mission;
    }

    public void addEmployee(final Employee employee) {
        if (employees.contains(employee)) {
            Gdx.app.error(Constants.TAG, "Employee added to Missionworker that was already part of missionworker");
        }
        employees.add(employee);
    }


    @Override
    public void timeStepChanged(final int step) {
        if (!employees.isEmpty() && !mission.isFinished()) {
            calculateMissionStep();
        }
    }

    /**
     * Gets called every clock step (every ~5s)
     */
    private void calculateMissionStep() {
        for (Employee em
                : employees) {
            for (MissionSkillRequirement req
                    : skillRequirements) {
                //if requirement is already met, don't work on skill
                if (req.getCurrentValue() >= req.getValueRequired()){continue;}
                int value = em.getSkillValue(req.getSkillType());
                workOnSkill(em, req, value);
            }
        }
        if (alreadyDone()) {
            mission.setFinished(true);
            Gdx.app.log(Constants.TAG, "Mission already over!");
            mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
            mission.setCompleted(true);
        }
    }

    /**
     * Gets called every step. Calculates an employees contribution to the overall skill requirement.
     * @param em Employee that is working
     * @param req The required Skill Object that holds the required and current skill values
     * @param value The employees skill value for that paricular skill
     */
    private void workOnSkill(final Employee em, final MissionSkillRequirement req, final int value) {
        float stepValue = value * (0.9f + RandomUtils.randomFloat() * 0.2f) * (1f / Constants.TIME_STEPS_PER_DAY);

        int dice = RandomUtils.randomInt(DICE_SIDES) + 1; //1-DICE_SIDES
        if (dice < em.getCriticalFailureChance()) {
            //criticalFailure
            req.incrementCurrentValue(0.5f * stepValue);
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.FAILURE, em);
        } else if (dice > DICE_SIDES - em.getCriticalSuccessChance()) {
            //criticalSuccess
            req.incrementCurrentValue(2 * stepValue);
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.SUCCESS, em);
            em.onMissionCriticalSuccess();
        } else {
            req.incrementCurrentValue(stepValue);
        }
    }

    private boolean alreadyDone() {
        int notFinishedSkills = 0;
        for (MissionSkillRequirement skillReq
                : skillRequirements) {
            if (!skillReq.isSuccessfull()) {
                notFinishedSkills++;
            }
        }
        return notFinishedSkills <= 0;
    }


    @Override
    public void dayChanged(final int days) {
        if (!mission.isFinished()) {
            Gdx.app.log(Constants.TAG, "Next day. Job: " + mission.getName() +  " Remaining mission days: " + remainingMissionDays);
            for (MissionSkillRequirement req
                    : skillRequirements) {
                Gdx.app.log(Constants.TAG, "Skill " + req.getSkillType().getDisplayName() + "(Current: " + req.getCurrentValue() + ", Needed: " + req.getValueRequired() + ")");
            }
            if (--remainingMissionDays == 0) {
                mission.setFinished(true);
                Gdx.app.log(Constants.TAG, "Mission over!");
                ArrayList<SkillType> failedSkills = new ArrayList<SkillType>(4);
                for (MissionSkillRequirement skillReq
                        : skillRequirements) {
                    if (!skillReq.isSuccessfull()) {
                        failedSkills.add(skillReq.getSkillType());
                    }
                }
                if (failedSkills.size() > 0) {
                    mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
                } else {
                    mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
                    mission.setCompleted(true);
                }

            }
        }
    }

    @Override
    public void weekChanged(final int week) {
        //Don't care
    }

    @Override
    public void timeChanged(final float time) {
        //Don't care
    }


    public boolean removeEmployee(Employee employee) {
        return employees.remove(employee);
    }

    public boolean hasNoWorkers() {
        return employees.isEmpty();
    }

    public List<MissionSkillRequirement> getSkillRequirements(){
        return skillRequirements;
    }

    public int getRemainingMissionDays() {
        return remainingMissionDays;
    }

    public int getPassedMissionDays() {
        return mission.getDuration() - remainingMissionDays;
    }

    public int getMissionDays() {
        return mission.getDuration();
    }
}
