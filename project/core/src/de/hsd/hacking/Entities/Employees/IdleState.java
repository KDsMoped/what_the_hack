package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 23.05.2017.
 */

public class IdleState extends EmployeeState {

    private float elapsedTime = 0f;
    private boolean moving;
    private float stayTime;

    public IdleState(Employee employee){
        super(employee);
        this.stayTime = MathUtils.random(20);
    }

    @Override
    EmployeeState act(float deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= stayTime){
            return new MovingState(employee);
        }

        return null;
    }

    @Override
    public void enter() {
        super.enter();
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Idle State");
        employee.setAnimationState(Employee.AnimState.IDLE);
    }

    @Override
    void leave() {

    }
}
