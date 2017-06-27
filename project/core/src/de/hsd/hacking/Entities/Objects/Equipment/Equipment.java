package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Objects.TouchableInteractableObject;
import de.hsd.hacking.Entities.Objects.TouchableObject;
import de.hsd.hacking.Entities.Team.Team;

/**
 * Created by domin on 31.05.2017.
 */

public abstract class Equipment extends TouchableInteractableObject {

    public enum EquipmentType {
        COMPUTER, SWITCH, COFFEEMAKER, MODEM, SERVER;
    }

    public enum EquipmentAttributeType {
        MONEY, BANDWIDTH, COMPUTATIONPOWER,
        SKILL_SOCIAL, SKILL_HARDWARE, SKILL_SOFTWARE, SKILL_NETWORK,
        SKILL_CRYPTO, SKILL_SEARCH, SKILL_ALLPURPOSE;
        //Unfortunately there doesn't seem to be an elegant way to mirror Employee.SkillType here.
    }

    protected String name;
    protected float price;
    protected EquipmentAttributeType attributeType;
    protected int attributeValue = 100;

    protected int level = 1;
    protected Team team;

    public Equipment(String name,
                     float price,
                     EquipmentAttributeType attributeType,
                     int attributeValue,
                     TextureRegion drawableRegion,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        setAttributeType(attributeType);
        setPrice(price);
        this.team = team.getInstance();
    }

    public void setAttributeType(EquipmentAttributeType attributeType) { this.attributeType = attributeType; }
    public EquipmentAttributeType getAttributeType() { return attributeType; }

    public void setAttributeValue(int value) { attributeValue = value; }
    public int getAttributeValue() { return attributeValue; }

    public void setPrice(float price) { this.price = price; }
    public float getPrice() { return price;}

    private int getLevel() { return level; }


    @Override
    public String getName()  {
        return "";
    }


}
