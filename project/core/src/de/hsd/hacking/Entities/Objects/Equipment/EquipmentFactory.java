package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 14.06.2017.
 */

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;

public class EquipmentFactory {

    public static Equipment getEquipment(EquipmentType type,
                                         Assets assets) {
        switch(type){
            case COMPUTER:
                return new Computer(assets);
            case SWITCH:
            case COFFEEMAKER:
            case MODEM:
                return new Modem(assets);
            case SERVER:
        }

        return null;
    }
}
