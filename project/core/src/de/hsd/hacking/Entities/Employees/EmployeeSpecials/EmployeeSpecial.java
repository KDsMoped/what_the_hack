package de.hsd.hacking.Entities.Employees.EmployeeSpecials;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Entities.Employees.Employee;

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
        GameTime.instance.addTimeChangedListener(this);
        employ();
    }

    protected void employ(){}

    public final void onDismiss(){
        GameTime.instance.removeTimeChangedListener(this);
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

    public int getCriticalFailureBonus() { return  0;}
    public int getCriticalSuccessBonus() { return  0;}

    public void onTouch(){}

    public boolean isHidden(){
        return false;
    }

    public abstract String getDisplayName();
    public abstract String getDescription();

    public abstract float getScoreCost();
}
