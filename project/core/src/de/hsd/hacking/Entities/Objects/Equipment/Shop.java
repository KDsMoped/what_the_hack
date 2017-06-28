package de.hsd.hacking.Entities.Objects.Equipment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by domin on 28.06.2017.
 */

public class Shop {

    private ArrayList<Equipment> shopItems = new ArrayList<Equipment>();

    private static Shop instance = null;

    private Shop() {
        Computer computer = new Computer();
        shopItems.add(computer);
        Modem modem = new Modem();
        shopItems.add(modem);
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        shopItems.add(coffeeMachine);
        Router router = new Router();
        shopItems.add(router);
    }

    public static Shop instance() {
        if (instance == null) {
            instance = new Shop();
        }
        return instance; }

    public ArrayList<Equipment> getShopItemList() { return shopItems; }

}
