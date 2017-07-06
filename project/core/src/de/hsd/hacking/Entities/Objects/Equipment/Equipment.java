package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Objects.TouchableInteractableObject;
import de.hsd.hacking.Entities.Team.Team;

/**
 * Created by domin on 31.05.2017.
 */

public abstract class Equipment extends TouchableInteractableObject {

    public enum EquipmentType {
        COMPUTER, SWITCH, COFFEEMACHINE, MODEM, SERVER;
    }

    /*
    public enum EquipmentAttributeType {
        MONEY, BANDWIDTH, COMPUTATIONPOWER,
        SKILL_SOCIAL, SKILL_HARDWARE, SKILL_SOFTWARE, SKILL_NETWORK,
        SKILL_CRYPTO, SKILL_SEARCH, SKILL_ALLPURPOSE;
        //Unfortunately there doesn't seem to be an elegant way to mirror Employee.SkillType here.
    }
    */

    protected String name;
    protected float price;
    //protected EquipmentAttributeType attributeType;
    //protected int attributeValue = 100;
    protected boolean isBought = false;

    protected int level = 1;
    protected Team team;

    public Equipment(String name,
                     float price,
                     //EquipmentAttributeType attributeType,
                     //int attributeValue,
                     TextureRegion drawableRegion,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        this.name = name;
        setPrice(price);
        //setAttributeType(attributeType);
        //setAttributeValue(attributeValue);
        this.team = team.instance();
    }

    //public void setAttributeType(EquipmentAttributeType attributeType) { this.attributeType = attributeType; }
    //public EquipmentAttributeType getAttributeType() { return attributeType; }

    //public void setAttributeValue(int attributeValue) { this.attributeValue = attributeValue; }
    //public int getAttributeValue() { return attributeValue; }

    public int getLevel() { return level; }

    public void setPrice(float price) { this.price = price; }
    public float getPrice() { return price*level;}

    // Bonuses
    public int getBandwidthBonus() { return 0; }
    public int getComputationPowerBonus() { return 0; }
    public int getSocialSkillBonus() { return 0; }
    public int getHardwareSkillBonus() { return 0; }
    public int getSoftwareSkillBonus() { return 0; }
    public int getNetworkSkillBonus() { return 0; }
    public int getCryptoSkillBonus() { return 0; }
    public int getSearchSkillBonus() { return 0; }
    public int getAllPurposeSkillBonus() { return 0; }


    public boolean isBought() { return isBought; }
    public void setBought(boolean isBought) {
        this.isBought = isBought;
    }

    @Override
    public String getName()  {
        return name;
    }

}
