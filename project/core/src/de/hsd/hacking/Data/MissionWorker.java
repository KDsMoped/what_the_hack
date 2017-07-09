package de.hsd.hacking.Data;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Entities.Employees.EmojiBubbleFactory;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.MissionSkillRequirement;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Employees.SkillType;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.RandomUtils;

/**
 * Created by Cuddl3s on 28.06.2017.
 */

public class MissionWorker implements TimeChangedListener {

    private Mission mission;
    private int remainingMissionDays;
    private List<MissionSkillRequirement> skillRequirements;
    private List<Employee> employees;

    public MissionWorker(Mission mission){
        this.mission = mission;
        this.remainingMissionDays = mission.getDuration();
        skillRequirements = new ArrayList<MissionSkillRequirement>(4);
        employees = new ArrayList<Employee>(4);
        for (Skill skill
                : mission.getSkill()) {
            skillRequirements.add(new MissionSkillRequirement(skill.getType(), skill.getValue() * mission.getDuration(), 0f));
        }
    }

    public Mission getMission() {
        return mission;
    }

    public void addEmployee(Employee employee) {

        if (employees.contains(employee)) Gdx.app.error(Constants.TAG, "Employee added to Missionworker that was already part of missionworker");
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
            for (MissionSkillRequirement req :
                    skillRequirements) {
                int value = em.getSkillValue(req.getSkillType());
                workOnSkill(em, req, value);
            }
        }
    }

    private void workOnSkill(Employee em, MissionSkillRequirement req, int value) {
        float stepValue = value * (0.9f + RandomUtils.randomFloat() * 0.2f) * (1 / 9f);

        int dice = RandomUtils.randomInt(20) + 1; //1-20
        if (dice < 1 + em.getCriticalFailureChance()){
            //criticalFailure
            req.incrementCurrentValue(0.5f);
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.FAILURE, em);
            MessageManager.instance().Warning("Employee " + em.getName() + " had a critical failure while working on " + req.getSkillType().getDisplayName());
        } else if (dice > 20 - em.getCriticalSuccessChance()){
            //criticalSuccess
            MessageManager.instance().Info("Employee " + em.getName() + " had a critical success while working on " + req.getSkillType().getDisplayName());
            req.incrementCurrentValue(2 * stepValue);
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.SUCCESS, em);
        } else {
            req.incrementCurrentValue(stepValue);
//            Gdx.app.log(Constants.TAG, "Employee " + em.getName() + " increased required skill value " + req.getSkillType().getDisplayName() + " by " + stepValue + " this step.");
        }

    }


    @Override
    public void dayChanged(final int days) {
        if (!employees.isEmpty() && !mission.isFinished()) {
            if (--remainingMissionDays == 0) {
                mission.setFinished(true);
                Gdx.app.log(Constants.TAG, "Mission over!");
                ArrayList<SkillType> failedSkills = new ArrayList<SkillType>(4);
                for (MissionSkillRequirement skillReq :
                        skillRequirements) {
                    if (!skillReq.isSuccessfull()) failedSkills.add(skillReq.getSkillType());
                }
                if (failedSkills.size() > 0) {
                    MessageManager.instance().Error("Mission failed, skill(s) to blame: " + failedSkills.toString());
                    mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
                } else {
                    MessageManager.instance().Info("Mission successfull! NICE!");
                    mission.notifyListeners(EventListener.EventType.MISSION_FINISHED);
                    mission.setCompleted(true);
                }

            } else {
                Gdx.app.log(Constants.TAG, "Next day. Remaining mission days: " + remainingMissionDays);
                for (MissionSkillRequirement req :
                        skillRequirements) {
                    Gdx.app.log(Constants.TAG, "Skill " + req.getSkillType().getDisplayName() + "(Current: " + req.getCurrentValue() + ", Needed: " + req.getValueRequired() + ")");
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
}
