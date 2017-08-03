package de.hsd.hacking.Entities.Objects.Equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.hsd.hacking.Data.DataContainer;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Objects.TouchableInteractableObject;
import de.hsd.hacking.Entities.Team.Team;

public abstract class Equipment extends TouchableInteractableObject implements DataContainer {
    protected Proto.Equipment.Builder data;
    protected boolean isPurchased = false;

    protected Team team;

    public Equipment(String name,
                     float price,
                     TextureRegion drawableRegion,
                     boolean blocking, Direction occupyDirection, int occupyAmount, Direction facingDirection) {
        super(drawableRegion, blocking, occupyDirection, occupyAmount, facingDirection);
        this.data = Proto.Equipment.newBuilder();
        data.setName(name);
        data.setPrice(price);
        data.setLevel(1);
        this.team = Team.instance();
    }

    public int getLevel() { return data.getLevel(); }
    public void setLevel(int level) {
        data.setLevel(level);
    }

    public void setPrice(float price) { data.setPrice(price); }
    public float getPrice() { return data.getPrice() * data.getLevel();}

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
        return data.getName();
    }

    public Proto.Equipment getData() {
        return data.build();
    }
}
