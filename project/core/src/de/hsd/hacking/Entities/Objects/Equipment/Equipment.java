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
        MONEY, BANDWIDTH, COMPUTATIONPOWER;
    }

    public enum EquipmentAttributeLevel {
        LOW, MID, HIGH;

        private static final EquipmentAttributeLevel[] VALUES = values();
        public static final int SIZE = VALUES.length;

        public static EquipmentAttributeLevel getRandomAttributeLevel() { return VALUES[MathUtils.random(SIZE - 1)]; }
    }

    private EquipmentAttributeLevel attributeLevel;
    private EquipmentAttributeType attributeType;
    private String name;
    private float price;
    private int attributeValue = 100;
    public Team team;

    public Equipment(TextureRegion drawableRegion, float price,
                     EquipmentAttributeType attributeType,
                     EquipmentAttributeLevel attributeLevel,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection, Team team) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        setAttributeType(attributeType);
        setAttributeLevel(attributeLevel);
        setPrice(price);
        this.team = team;
    }

    public void setAttributeType(EquipmentAttributeType attributeType) { this.attributeType = attributeType; }
    public void setAttributeLevel(EquipmentAttributeLevel attributeLevel) { this.attributeLevel = attributeLevel; }
    public EquipmentAttributeType getAttributeType() { return attributeType; }
    public EquipmentAttributeLevel getAttributeLevel() { return attributeLevel; }

    public void setAttributeValue(int value) { attributeValue = value; }
    public int getAttributeValue() { return attributeValue; }

    public void setPrice(float price) { this.price = price; }
    public float getPrice() { return price;}


    @Override
    public String getName()  {
        return "";
    }

    public abstract EquipmentType getType();

}
