package de.hsd.hacking.Entities.Objects.Equipment.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Tile.TileMap;
import de.hsd.hacking.Entities.Objects.Desk;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.States.EmployeeState;

import static de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager.instance;

public class CoffeeMachine extends Equipment implements Upgradable {

    private TextureRegion stillRegion;
    private Animation<TextureRegion> cooking;
    private TextureRegion coffeeDone;
    private float elapsedTime = 0f;

    private int state = 1;

    public CoffeeMachine(){
        super("Coffee Maker", 100, Assets.instance().coffeemachine.get(0), true, Direction.DOWN, 0, Direction.DOWN);
        this.data.setType(Proto.Equipment.EquipmentType.CoffeeMachine);

        Assets assets = Assets.instance();
        this.stillRegion = assets.coffeemachine.get(0);
        this.cooking = new Animation<TextureRegion>(.2f, assets.coffeemachine.get(1), assets.coffeemachine.get(2));
        this.coffeeDone = assets.coffeemachine.get(3);
    }

    public void upgrade() {
        data.setLevel(data.getLevel() + 1);
        team.updateResources();
    }

    public int getMaxLevel() { return 5; }

    @Override
    public int getAllPurposeSkillBonus() { return data.getLevel() * 1; }

    @Override
    public void setPurchased(boolean isPurchased) {
        super.setPurchased(isPurchased);

        TileMap tileMap = GameStage.instance().getTileMap();

        Desk desk = new Desk(Assets.instance(), Direction.RIGHT, 1);
        tileMap.addObject(10, 0, desk);
        //CoffeeMachine coffeeMachine = new CoffeeMachine();
        desk.setContainedObject(this, 0);
        GameStage.instance().addTouchable(this);
    }

    @Override
    public TextureRegionDrawable getIcon() {
        return Assets.instance().coffeemachine_icon;
    }

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
