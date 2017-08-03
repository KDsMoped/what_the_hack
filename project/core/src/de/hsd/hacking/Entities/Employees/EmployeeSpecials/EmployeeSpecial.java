package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.SkillType;

/**
 * Employee specials are held by an employee and alter his skills and behaviour in different ways. Needs to be extended.
 *
 * @author Hendrik
 */
public abstract class EmployeeSpecial extends Actor implements TimeChangedListener {

    protected final Employee employee;

    public EmployeeSpecial(Employee employee) {
        super();

        this.employee = employee;
    }

    @Override
    public void act(float delta){}

    @Override
    public void draw(Batch batch, float parentAlpha){}

    public final void onEmploy(){
        GameTime.instance().addTimeChangedListener(this);
        employ();
    }

    protected void employ(){}

    public final void onDismiss(){
        GameTime.instance().removeTimeChangedListener(this);
        dismiss();
    }

    protected void dismiss(){}

    /**
     * Gets called when the internal game time has changed, e.g each frame.
     *
     * @param time The current game time in the range 0-1.0
     */
    public void timeChanged(float time){}

    /**
     * Gets called when the internal game time has completed one clock time step ( 1/9 day).
     */
    public void timeStepChanged(int step){}

    /**
     * Gets called when the game day changes and has the new day as parameter.
     *
     * @param days new day in the range 1-365
     */
    public void dayChanged(int days){}

    /**
     * Gets called when the game week changes and has the new week as parameter.
     *
     * @param week
     */
    public void weekChanged(int week){}

    /**
     * Override this if this special should affect skills of this employee. Value is applied relatively at the end of the calculation.
     * @param type
     * @return
     */
    public float getSkillRelativeFactor(SkillType type) { return  1;}

    /**
     * Override this if this special should affect skills of this employee. Value is applied absolutely during the calculation.
     * @param type
     * @return
     */
    public int getSkillAbsoluteBonus(SkillType type) { return  0;}

    public int getCriticalFailureBonus() { return  0;}
    public int getCriticalSuccessBonus() { return  0;}

    /**
     * Override this if this special should affect hiring cost of this employee. Value is applied in absolute values at the end of the calculation.
     * @return
     */
    public int getHiringCostAbsoluteBonus() { return  0;}

    /**
     * Override this if this special should affect hiring cost of this employee. Value is applied relatively during the calculation.
     * @return
     */
    public float getHiringCostRelativeFactor() { return  1;}

    /**
     * Override this if this special should affect salary of this employee. Value is applied in absolute values at the end of the calculation.
     * @return
     */
    public int getSalaryAbsoluteBonus() { return  0;}

    /**
     * Override this if this special should affect salary of this employee. Value is applied relatively during the calculation.
     * @return
     */
    public float getSalaryRelativeFactor() { return  1;}

    public void onTouch(){}

    public void onMissionCompleted(){}

    public void onLevelUp(){}

    /**
     * Override this if the special should be hidden in UI.
     * @return
     */
    public boolean isHidden(){
        return false;
    }

    /**
     * Override this if the special cannot be learned during the game.
     * @return
     */
    public boolean isLearnable(){
        return true;
    }

    /**
     * Override this if learning this special has requirements. Return false if it cannot be learned.
     * @return
     */
    public boolean isApplicable(){
        return true;
    }

    /**
     * Returns the display name of this special as shown in UI.
     * @return
     */
    public abstract String getDisplayName();

    /**
     * Returns the display description of this special as shown in UI.
     * @return
     */
    public abstract String getDescription();

    /**
     * Returns the balancing score cost of this special. Use negative values for negative specials and positive for positive specials.
     * @return
     */
    public abstract float getScoreCost();
}
