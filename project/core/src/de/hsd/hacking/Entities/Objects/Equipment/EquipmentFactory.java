package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 14.06.2017.
 */

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentAttributeLevel;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;
import de.hsd.hacking.Entities.Team.Team;

public class EquipmentFactory {

    public static Equipment getEquipment(EquipmentType type,
                                         EquipmentAttributeLevel attributeLevel,
                                         float price,
                                         Assets assets, Team team) {
        switch(type){
            case COMPUTER:
                return new Computer(price, attributeLevel, assets, team);
            case SWITCH:
            case COFFEEMAKER:
            case MODEM:
                return new Modem(price, attributeLevel, assets, team);
            case SERVER:
        }

        return null;
    }
}
