package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private int tintFrames = 0;
    private Chair workingChair;

    private int maxLevel;
    private int mul;


    public Computer() {
        super("Super Computer 3000", 100, EquipmentAttributeType.COMPUTATIONPOWER, 100,
                Assets.instance().computer.get(0), true, Direction.DOWN, 0, Direction.DOWN);
        Assets assets = Assets.instance();
        this.stillRegion = assets.computer.get(0);
        this.animation = new Animation<TextureRegion>(.2f, assets.computer.get(1), assets.computer.get(2), assets.computer.get(3));
    }

    //Upgrade functions
    public void upgrade() {
        level++;
        attributeValue += 100;
    }

    public void setMaxLevel() {
        maxLevel = 5;
    }

    public void setUpgradePriceMultiplier() {
        mul = 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (tintFrames > 0) {
            batch.setColor(Color.RED);
        }
        super.draw(batch, parentAlpha);
        if (tintFrames > 0) {
            batch.setColor(Color.WHITE);
        }
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
            //TODO EVENT für Ärgernis des Charakters
            return new IdleState(e);
        }

        Gdx.app.log(Constants.TAG, "Interacted with Computer!");
        Gdx.app.log(Constants.TAG, "Trying to Send to chair...");
        if (e.getMovementProvider().getDiscreteTile(workingChair.getPosition()).isMovableTo()) {
            //TODO Event für OK!
            occupy();
            elapsedTime = 0f;
            return new MovingState(e, e.getMovementProvider().getDiscreteTile(workingChair.getPosition()));
        } else {
            //TODO EVENT für Ärgernis des Charakters
            return new IdleState(e);
        }

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
        if (team.isEmployeeSelected()) {
            team.getSelectedEmployee().setState(interact(team.getSelectedEmployee()));
            team.deselectEmployee();
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
