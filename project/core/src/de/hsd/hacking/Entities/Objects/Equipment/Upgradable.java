package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 05.06.2017.
 */

public interface Upgradable {
    enum UpgradeLevel {
        ONE, TWO, THREE, FOUR, FIVE;
    }

    void upgrade();

    int getLevel();

    void setInitialLevel(int level);
}
