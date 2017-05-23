package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.MovementProvider;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 24.05.2017.
 */

public class MovingState extends EmployeeState {

    private static final float SPEED = 40f;

    private Vector2 destinationPos;

    public MovingState(Employee employee) {
        super(employee);
        this.destinationPos = employee.getMovementProvider().getNextMovetoPoint(employee);
    }

    @Override
    EmployeeState act(float deltaTime) {
        if (destinationPos.cpy().sub(employee.getPosition()).len() > 0.5f){
            employee.getPosition().add(destinationPos.cpy().sub(employee.getPosition()).nor().scl(SPEED).scl(deltaTime));
            return null;
        }else{
            return new IdleState(employee);
        }
    }

    @Override
    public void enter() {
        super.enter();
        Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Moving State");
        employee.setAnimationState(Employee.AnimState.MOVING);
    }

    @Override
    void leave() {

    }
}
