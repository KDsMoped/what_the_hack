package de.hsd.hacking.Entities.Objects.Equipment;

import de.hsd.hacking.Entities.Objects.Equipment.Equipment.EquipmentType;

/**
 * Created by domin on 14.06.2017.
 */

public class EquipmentFactory {

    public static Equipment getEquipment(EquipmentType type) {
        switch(type){
            case COMPUTER:
                return new Computer();
            case SWITCH:
            case COFFEEMACHINE:
                return new CoffeeMachine();
            case MODEM:
                return new Modem();
            case SERVER:
        }

        return null;
    }
}
