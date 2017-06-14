package de.hsd.hacking.Entities.Equipment;

/**
 * Created by domin on 14.06.2017.
 */

import de.hsd.hacking.Assets.Assets;

public class EquipmentFactory {

    public static Equipment getEquipment(Equipment.EquipmentType type,
                                         Equipment.EquipmentAttributeLevel attributeLevel,
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
