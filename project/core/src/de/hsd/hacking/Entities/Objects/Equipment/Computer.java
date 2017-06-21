package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;
import de.hsd.hacking.Entities.Employees.IdleState;
import de.hsd.hacking.Entities.Employees.MovingState;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Computer extends Equipment implements Upgradable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> animation;
    private boolean on;
    private float elapsedTime = 0f;
    private Chair workingChair;

    private int level = 0;
    private Assets assets;


    public Computer(float price, EquipmentAttributeLevel attributeLevel, Assets assets, Team team) {
        super(assets.computer.get(0), price, EquipmentAttributeType.COMPUTATIONPOWER, attributeLevel, true, Direction.DOWN, 0, Direction.DOWN, team);
        this.stillRegion = assets.computer.get(0);
        this.animation = new Animation<TextureRegion>(.2f, assets.computer.get(1), assets.computer.get(2), assets.computer.get(3));
    }

    public EquipmentType getType() { return EquipmentType.COMPUTER; }

    //Upgrade functions
    public void upgrade() {
        level++;
        setAttributeValue(getAttributeValue() + 100);

    }
    public int getLevel() { return level; }
    public void setInitialLevel(int level) { this.level = level; }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        if (on){
            setDrawableRegion(animation.getKeyFrame(elapsedTime, true));
        }else{
            setDrawableRegion(stillRegion);
        }
    }

    @Override
    public EmployeeState interact(Employee e) {
        if (isOccupied()){
            return new IdleState(e);
        }
        occupy();
        elapsedTime = 0f;
        Gdx.app.log(Constants.TAG, "Interacted with Computer!");
        Gdx.app.log(Constants.TAG, "Sending to chair...");
        return new MovingState(e, e.getMovementProvider().getDiscreteTile(workingChair.getPosition()));
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
        if (team.isEmployeeSelected()){
            team.getSelectedEmployee().getState().cancel();
            team.getSelectedEmployee().setState(interact(team.getSelectedEmployee()));
            team.getSelectedEmployee().toggleSelected();
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
}