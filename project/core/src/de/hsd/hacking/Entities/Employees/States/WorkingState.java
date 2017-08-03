package de.hsd.hacking.Entities.Employees.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.Missions.MissionManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Utils.Constants;

public class WorkingState extends EmployeeState implements EventListener {

    private Vector2 workingPosition;
    private Computer computer;
    private float timeBeforeIdle;
    private float elapsedTime = 0f;
    private boolean workingOnMission;
    private boolean missionFinished;

    public WorkingState(Employee employee, Vector2 position, Computer computer) {
        super(employee);
        this.workingPosition = position;
        employee.setPosition(position.cpy());
        employee.removeFromOccupyingTile();
        employee.getMovementProvider().getDiscreteTile(position).setOccupyingEmployee(employee);

        this.computer = computer;

    }

    @Override
    public EmployeeState act(float deltaTime) {
        if (!isCanceled()) {
            if (workingOnMission) {
                if (missionFinished) {
                    employee.onMissionCompleted();
                    return new IdleState(employee);
                }
                return null;
            }
        }
        return new IdleState(employee);
    }

    @Override
    public void enter() {
        if (Constants.DEBUG) Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " transitioning to Working State");
        employee.setAnimationState(Proto.Employee.AnimState.WORKING);
        Interactable workPlace = (Interactable) employee.getMovementProvider().getDiscreteTile(workingPosition).getObject();
        boolean left = workPlace.getFacingDirection() == Direction.LEFT || workPlace.getFacingDirection() == Direction.DOWN;
//        boolean backFaced = workPlace.getFacingDirection() == Direction.UP || workPlace.getFacingDirection() == Direction.LEFT;
        if (left) employee.flipHorizontal(false);
        computer.setOn(true);

        //Working
        if (employee.getCurrentMission() != null) {
            if (!employee.getCurrentMission().isFinished()) {
                MissionManager.instance().startWorking(employee);
                this.workingOnMission = true;
                this.missionFinished = false;
                employee.getCurrentMission().addListener(this);
                Gdx.app.log(Constants.TAG, "Employee " + employee.getName() + " started working on " + employee.getCurrentMission().getName());
            } else {
                cancel();
            }
        } else {
            this.timeBeforeIdle = MathUtils.random(10f, 30f);
            this.workingOnMission = false;
        }
    }

    @Override
    public void leave() {
        reset();
    }

    @Override
    public void cancel() {
        reset();
        canceled = true;
    }

    private void reset() {
        employee.setAnimationState(Proto.Employee.AnimState.IDLE);
        computer.setOn(false);
        computer.deOccupy();
        if (employee.getCurrentMission() != null) {
            MissionManager.instance().stopWorking(employee);
        }
    }

    @Override
    public String getDisplayName() {
        return "Working";
    }

    @Override
    public void OnEvent(EventType type, Object sender) {
        switch (type) {
            case MISSION_STARTED:
                break;
            case MISSION_FINISHED:
                missionFinished = true;
                break;
            case MISSION_ABORTED:
                Gdx.app.log(Constants.TAG, "Mission working was aborted for Employee: " + employee.getName());
                cancel();
            default:
                break;
        }
    }
}
