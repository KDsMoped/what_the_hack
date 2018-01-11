package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Missions.Mission;
import de.hsd.hacking.Entities.Employees.EmojiBubbleFactory;
import de.hsd.hacking.Entities.Employees.States.WaitingState;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Entities.Team.Workspace;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.UI.Mission.MissionAllocatorPopup;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Callback.MissionCallback;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;
import de.hsd.hacking.Entities.Employees.States.IdleState;
import de.hsd.hacking.Entities.Employees.States.MovingState;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Utils.Constants;

/**
 * An Equipment Item that can be assigned to a workspace to complete missions.
 * @author Dominik, Florian
 */

public class Computer extends Equipment implements Upgradable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> animation;
    private boolean on;
    private float elapsedTime = 0f;
    private int tintFrames = 0;
    private Chair workingChair;

    private Workspace workspace;

    public Computer(String name, Workspace workspace) {
        super(name, 1600, Assets.instance().computer.get(0), true, Direction.DOWN, 0, Direction.DOWN);
        this.data.setType(Proto.Equipment.EquipmentType.Computer);

        Assets assets = Assets.instance();
        this.stillRegion = assets.computer.get(0);
        this.animation = new Animation<TextureRegion>(.2f, assets.computer.get(1), assets.computer.get(2), assets.computer.get(3));
        this.workspace = workspace;
    }

    //Upgrade functions
    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        teamManager.updateResources();
    }

    public int getMaxLevel() { return 5; }

    @Override
    public int getComputationPowerBonus() { return data.getLevel() * 100; }

    @Override
    public void onPurchase(boolean isPurchased) {
        super.onPurchase(isPurchased);

        if (workspace != null)
            workspace.addComputer(this);
    }

    @Override
    public void addToTileMap(){
        int number = Integer.parseInt(getName().substring(getName().length() - 1)) - 1;
        setWorkspace(GameStage.instance().getWorkspaces().get(number));
    }

    @Override
    public TextureRegionDrawable getIcon() {
        return Assets.instance().computer_icon;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        if (tintFrames > 0) {
            tintFrames--;
        }
        if (on) {
            setDrawableRegion(animation.getKeyFrame(elapsedTime, true));
        } else {
            setDrawableRegion(stillRegion);
        }
    }

    @Override
    public EmployeeState interact(Employee e) {
        if (isOccupied()) {
            Gdx.app.log(Constants.TAG, "OCCUPIED!!!");
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.NO, e);
            return new IdleState(e);
        }

        Gdx.app.log(Constants.TAG, "Interacted with Computer!");
        Gdx.app.log(Constants.TAG, "Trying to Send to chair...");
        if (e.getMovementProvider().getDiscreteTile(workingChair.getPosition()).isMovableTo()) {
            return askForMission(e);
        } else {
            EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.NO, e);
            return new IdleState(e);
        }
    }

    /**
     * Lets employee wait while selecting a mission.
     * @param e Employee that should wait.
     * @return WaitingState object with following States set.
     */
    private WaitingState askForMission(final Employee e) {
        occupy();
        elapsedTime = 0f;

        final WaitingState waitingState = new WaitingState(e);

        GameStage.instance().addPopup(new MissionAllocatorPopup(new MissionCallback() {
            @Override
            public void callback(Mission mission) {
                //a mission was chosen
                e.setCurrentMission(mission);
                EmojiBubbleFactory.show(EmojiBubbleFactory.EmojiType.OK, e);
                waitingState.setFollowingState(new MovingState(e, e.getMovementProvider().getDiscreteTile(workingChair.getPosition())));
            }
        }, new Callback() {
            @Override
            public void callback() {
                //dialogue was cancelled
                deOccupy();
                waitingState.setFollowingState(new IdleState(e));
            }
        }));

        return waitingState;
    }

    private void OnCancelJob(Employee e) {

    }

    private void OnSelectMission(Employee e, Mission mission) {

    }

    @Override
    public void occupy() {
        setOccupied(true);
    }

    @Override
    public void deOccupy() {
        setOccupied(false);
        workingChair.deOccupy();
    }

    @Override
    public boolean isDelegatingInteraction() {
        return true;
    }

    @Override
    public void onTouch() {
        tintFrames += 10;
        if (teamManager.isEmployeeSelected()) {
            teamManager.getSelectedEmployee().getState().cancel();
            teamManager.getSelectedEmployee().setState(interact(teamManager.getSelectedEmployee()));
            teamManager.deselectEmployee();
        }


        //TODO show stats etc...
    }

    public Chair getWorkingChair() {
        return workingChair;
    }

    public void setWorkingChair(Chair workingChair) {
        this.workingChair = workingChair;
        workingChair.setComputer(this);
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        workspace.addComputer(this);
    }
}
