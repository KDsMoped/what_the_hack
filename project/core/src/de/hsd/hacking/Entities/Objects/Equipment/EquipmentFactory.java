package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 14.06.2017.
 */

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentAttributeLevel;

public class EquipmentFactory {

    public static Equipment getEquipment(EquipmentType type,
                                         EquipmentAttributeLevel attributeLevel,
                                         float price,
                                         Assets assets) {
        switch(type){
            case COMPUTER:
                return new Computer(price, attributeLevel, assets);
            case SWITCH:
            case COFFEEMAKER:
            case MODEM:
            case SERVER:
        }

        return null;
    }
}
