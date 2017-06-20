package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;
import de.hsd.hacking.Entities.Employees.IdleState;
import de.hsd.hacking.Entities.Employees.MovingState;
import de.hsd.hacking.Entities.Objects.Chair;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Computer extends Equipment implements Upgradable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> animation;
    private boolean on;
    private float elapsedTime = 0f;
    private int level;
    private Chair workingChair;


    public Computer(float price, EquipmentAttributeLevel attributeLevel, Assets assets) {
        super(assets.computer.get(0), price, EquipmentAttributeType.COMPUTATIONPOWER, attributeLevel, true, Direction.DOWN, 0);
        this.stillRegion = assets.computer.get(0);
        this.animation = new Animation<TextureRegion>(.2f, assets.computer.get(1), assets.computer.get(2), assets.computer.get(3));
    }

    public EquipmentType getType() { return EquipmentType.COMPUTER; }

    //Upgrade functions
    public void upgrade() {}
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
        on = !on;
        Gdx.app.log(Constants.TAG, "Interacted with Computer!");
        return new MovingState(e, e.getMovementProvider().getTile(workingChair.getPosition().cpy().add(1f,1f)));
    }

    @Override
    public void occupy() {
        setOccupied(true);
    }

    @Override
    public void deOccupy() {
        setOccupied(false);
    }

    @Override
    public boolean isDelegatingInteraction() {
        return true;
    }

    @Override
    public void onTouch() {
        //TODO show stats etc...
    }

    public Chair getWorkingChair() {
        return workingChair;
    }

    public void setWorkingChair(Chair workingChair) {
        this.workingChair = workingChair;
    }
}
