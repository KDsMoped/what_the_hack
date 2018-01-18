package de.hsd.hacking.Data.Missions;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Proto;

/**
 * This class represents a mission.
 *
 * @author Hendrik, Julian
 */
public class Mission implements EventSender, DataContainer {
    private ArrayList<EventListener> listeners;    
    Proto.Mission.Builder data;

    public Mission() {
        data = Proto.Mission.newBuilder();
        data.setMissionNumber(-1);
        this.listeners = new ArrayList<EventListener>();
    }

    /**
     * Restore the mission with a protobuf builder.
     * @param builder protobuf mission builder object.
     */
    public Mission(Proto.Mission.Builder builder) {
        this.data = builder;
        this.listeners = new ArrayList<EventListener>();
    }

    public Mission(MissionHolder holder) {
        data = Proto.Mission.newBuilder();
        data.setName(holder.getName());
        data.setDescription(holder.getDescription());
        data.setOnSuccess(holder.getOnSuccess());
        data.setOnFail(holder.getOnFail());
        data.setDuration(holder.getDuration());
        data.setDifficulity(holder.getDifficulty());
        data.setHardness(holder.getHardness());
        for (SkillHolder h: holder.getSkill()) {
            Proto.Skill.Builder skill = Proto.Skill.newBuilder();
            skill.setType(h.getType());
            skill.setValue(h.getValue());
            data.addSkills(skill);
        }
        data.setRisk(holder.getRisk());
        data.setMinLevel(holder.getMinLevel());
        data.setMaxLevel(holder.getMaxLevel());
        this.listeners = new ArrayList<EventListener>();
    }

    /**
     * Start the mission.
     */
    public void Start() {
        notifyListeners(EventListener.EventType.MISSION_STARTED);
    }

    /**
     * Abort the mission.
     */
    public void Abort() {
        data.setFinished(true);
        notifyListeners(EventListener.EventType.MISSION_ABORTED);
    }

    @Override
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(EventListener.EventType type) {

        for (EventListener listener : listeners) {
            listener.OnEvent(type, this);
        }
    }

    public int getUsedBandwidth() {
        return data.getUsedBandwidth();
    }

    public void setUsedBandwidth(int usedBandwidth) {
        data.setUsedBandwidth(usedBandwidth);
    }

    public String getName() {
        return data.getName();
    }

    public void setName(String name) {
        data.setName(name);
    }

    public String getSuccessText() {
        return data.getOnSuccess();
    }

    public String getFailText() {
        return data.getOnFail();
    }

    public boolean hasFailText(){
        return data.getOnFail().length() > 0;
    }

    public boolean hasSuccessText(){
        return data.getOnSuccess().length() > 0;
    }

    public void setSuccessText(String onSuccess) {
        data.setOnSuccess(onSuccess);
    }

    public void setFailText(String onFail) {
        data.setOnFail(onFail);
    }

    public String getDescription() {
        return data.getDescription();
    }

    public void setDescription(String description) {
        data.setDescription(description);
    }

    public int getDuration() {
        return data.getDuration();
    }

    public void setDuration(int duration) {
        data.setDuration(duration);
    }

    public int getDifficulty() {
        return data.getDifficulity();
    }

    public void setDifficulty(int difficulty) {
        data.setDifficulity(difficulty);
    }

    public void setMinLevel(int level) {
        data.setMinLevel(level);
    }

    public void setMaxLevel(int level) {
        data.setMaxLevel(level);
    }

    public int getMinLevel() {
        return data.getMinLevel();
    }

    public void setRemainingDays(int remainingDays){
        data.setRemainingDays(remainingDays);
    }

    public int getRemainingDays(){
        return data.getRemainingDays();
    }

    public int getMaxLevel() {

        if(data.getMaxLevel() < 1) return Integer.MAX_VALUE;
        return data.getMaxLevel();
    }

    public List<Skill> getSkill() {
        List<Skill> lst = new ArrayList<Skill>();
        for (Proto.Skill.Builder s:data.getSkillsBuilderList()) {
            lst.add(new Skill(s));
        }

        return lst;
    }

    public void setSkill(List<Skill> skill) {
        for (Skill s:skill) {
            data.clearSkills();
            Proto.Skill.Builder proto = Proto.Skill.newBuilder();
            proto.setValue(s.getValue());
            proto.setType(s.getType().skillType);
            data.addSkills(proto);
        }
    }

    public float getRisk() {
        return data.getRisk();
    }

    public void setRisk(float risk) {
        data.setRisk(risk);
    }


    public void setMissionNumber(int missionNumber) {
        data.setMissionNumber(missionNumber);
    }

    public int getMissionNumber() {
        return data.getMissionNumber();
    }

    public boolean isFinished() {
        return data.getFinished();
    }

    public void setFinished(final boolean finished) {
        data.setFinished(finished);
    }

    /**
     * Returns true if this mission has been completed successfully.
     * @return
     */
    public boolean isCompleted() {
        return data.getCompleted();
    }

    public void setCompleted(boolean completed) {
        data.setCompleted(completed);
    }


    public float getHardness() {
        return data.getHardness();
    }

    public void setHardness(float hardness) {
        data.setHardness(hardness);
    }

    public int getRewardMoney() {
        return data.getRewardMoney();
    }

    public void setRewardMoney(int reward) {
        data.setRewardMoney(reward);
    }

    /**
     * Returns the name of the mission with a maximum length of characters.
     * @return shortened mission name.
     */
    public String getShortenedName(int maxLength){
        String name = data.getName();

        if (name.length() < maxLength) return name;
        else return name.substring(0, maxLength - 3) + "..";
    }

    public Proto.Mission getData() {
        return data.build();
    }
}
