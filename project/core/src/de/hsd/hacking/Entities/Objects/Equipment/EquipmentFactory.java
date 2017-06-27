package de.hsd.hacking.Entities.Objects.Equipment;

/**
 * Created by domin on 14.06.2017.
 */

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;

public class EquipmentFactory {

    public static Equipment getEquipment(EquipmentType type) {
        switch(type){
            case COMPUTER:
                return new Computer();
            case SWITCH:
            case COFFEEMAKER:
            case MODEM:
                return new Modem();
            case SERVER:
        }

        return null;
    }
}
