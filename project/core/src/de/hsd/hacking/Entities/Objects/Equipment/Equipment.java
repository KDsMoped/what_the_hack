package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Objects.TouchableInteractableObject;

/**
 * This is the base class for every Item, providing basic Equipment functionality.
 * Objects implementing this class need to override the respective methods for desired
 * behavior.
 * @author Dominik, Florian
 */

public abstract class Equipment extends TouchableInteractableObject implements DataContainer {
    protected Proto.Equipment.Builder data;
    protected boolean isPurchased = false;

    protected TeamManager teamManager;

    public Equipment(String name,
                     float price,
                     TextureRegion drawableRegion,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        this.data = Proto.Equipment.newBuilder();
        data.setName(name);
        data.setPrice(price);
        data.setLevel(1);
        this.teamManager = TeamManager.instance();
    }

    public int getLevel() { return data.getLevel(); }
    public void setLevel(int level) {
        data.setLevel(level);
    }

    public void setPrice(float price) { data.setPrice(price); }
    public float getPrice() { return data.getPrice() * data.getLevel();}

    // Bonuses

    /**
     * Returns 0 as the default Bandwidth increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getBandwidthBonus() { return 0; }

    /**
     * Returns 0 as the default Computation Power increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getComputationPowerBonus() { return 0; }

    /**
     * Returns 0 as the default Social Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getSocialSkillBonus() { return 0; }

    /**
     * Returns 0 as the default Hardware Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getHardwareSkillBonus() { return 0; }

    /**
     * Returns 0 as the default Software Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getSoftwareSkillBonus() { return 0; }

    /**
     * Returns 0 as the default Network Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getNetworkSkillBonus() { return 0; }

    /**
     * Returns 0 as the default Crypto Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getCryptoSkillBonus() { return 0; }

    /**
     * Returns 0 as the default Search Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getSearchSkillBonus() { return 0; }

    /**
     * Returns 0 as the default All Purpose Skill increase.
     * Override this to specify the actual bonus value.
     * @return 0
     */
    public int getAllPurposeSkillBonus() { return 0; }


    /**
     * Is called during <code>buyItem()</code> in the {@link EquipmentManager}.
     * Override this to specify the desired behavior when the Item is purchased.
     * @param isPurchased should be true after purchase
     */
    public void onPurchase(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public TextureRegionDrawable getIcon() { return null; }

    @Override
    public String getName()  {
        return data.getName();
    }

    public Proto.Equipment getData() {
        return data.build();
    }
}
