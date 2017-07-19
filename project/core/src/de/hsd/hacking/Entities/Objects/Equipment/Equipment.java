package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

    protected String name;
    protected float price;
    protected boolean isPurchased = false;

    protected int level = 1;
    protected Team team;

    public Equipment(String name,
                     float price,
                     TextureRegion drawableRegion,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        this.name = name;
        setPrice(price);
        this.team = Team.instance();
    }

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


    public boolean isPurchased() { return isPurchased; }
    public void setPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public TextureRegionDrawable getIcon() { return null; }

    @Override
    public String getName()  {
        return name;
    }

}
