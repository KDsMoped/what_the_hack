package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 05.06.2017.
 */

public interface Upgradable {
    void upgrade();
    int getLevel();
    void setMaxLevel();
    void setUpgradePriceMultiplier();
}
