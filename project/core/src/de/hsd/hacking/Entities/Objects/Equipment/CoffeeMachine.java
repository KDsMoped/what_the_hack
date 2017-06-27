package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeState;

public class CoffeeMachine extends Equipment implements Upgradable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> cooking;
    private TextureRegion coffeeDone;
    private float elapsedTime = 0f;

    private int state = 1;

    int maxLevel;
    float mul;

    public CoffeeMachine(){
        super("Coffee Maker 5000", 100, EquipmentAttributeType.SKILL_ALLPURPOSE, 5, Assets.instance().coffeemachine.get(0), true, Direction.DOWN, 0, Direction.DOWN);

        Assets assets = Assets.instance();
        this.stillRegion = assets.coffeemachine.get(0);
        this.cooking = new Animation<TextureRegion>(.2f, assets.coffeemachine.get(1), assets.coffeemachine.get(2));
        this.coffeeDone = assets.coffeemachine.get(3);
    }

    public void upgrade() {
        level++;
        attributeValue += 5;
    }

    public void setMaxLevel() {maxLevel = 5; }
    public void setUpgradePriceMultiplier() { mul = 2; }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;

        switch (state) {
            case 0:
                setDrawableRegion(stillRegion);
                break;

            case 1:
                setDrawableRegion(cooking.getKeyFrame(elapsedTime, true));
                break;

            case 2:
                setDrawableRegion(coffeeDone);
                break;
        }
    }

    @Override
    public void onTouch() {
        state++;
        if(state > 2) state = 0;
    }

    @Override
    public EmployeeState interact(Employee e) {
        return null;
    }

    @Override
    public void occupy() {

    }

    @Override
    public void deOccupy() {

    }

    @Override
    public boolean isDelegatingInteraction() {
        return false;
    }

}
