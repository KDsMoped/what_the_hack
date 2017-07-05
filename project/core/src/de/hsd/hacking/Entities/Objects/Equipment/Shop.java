package de.hsd.hacking.Entities.Objects.Equipment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hsd.hacking.Entities.Team.Team;

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
        return instance;
    }

    public int buyItem(int index) {
        Equipment e = shopItems.get(index);
        Team team = Team.instance();
        int price = (int)e.getPrice();
        if (team.resources.money < price)
            return 1;
        team.reduceResource(Equipment.EquipmentAttributeType.MONEY, price);
        team.addEquipment(e);
        return 0;
    }

    public int buyItem(Equipment equipment) {
        int index = shopItems.indexOf(equipment);
        Team team = Team.instance();
        int price = (int)equipment.getPrice();
        if (team.resources.money < price)
            return 1;
        team.reduceResource(Equipment.EquipmentAttributeType.MONEY, price);
        team.addEquipment(equipment);
        equipment.setBought(true);
        return 0;
    }

    public int upgradeItem(Equipment equipment) {
        Team team = Team.instance();
        int price = (int)equipment.getPrice();
        if (team.resources.money < price)
            return 1;
        team.reduceResource(Equipment.EquipmentAttributeType.MONEY, price);
        ((Upgradable) equipment).upgrade();
        return 0;
    }


    public ArrayList<Equipment> getShopItemList() { return shopItems; }

}
