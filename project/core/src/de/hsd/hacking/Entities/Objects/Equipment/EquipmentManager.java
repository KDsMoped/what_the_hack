package de.hsd.hacking.Entities.Objects.Equipment;

import java.util.ArrayList;

import de.hsd.hacking.Entities.Team.Team;

/**
 * Created by domin on 28.06.2017.
 */

public class EquipmentManager {

    private ArrayList<Equipment> shopItems = new ArrayList<Equipment>();
    private ArrayList<Equipment> purchasedItems = new ArrayList<Equipment>();

    private static EquipmentManager instance = null;

    private EquipmentManager() {
        Computer computer = new Computer();
        shopItems.add(computer);
        Modem modem = new Modem();
        shopItems.add(modem);
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        shopItems.add(coffeeMachine);
        Router router = new Router();
        shopItems.add(router);
    }

    public static EquipmentManager instance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    /*
    public int buyItem(int index) {
        Equipment e = shopItems.get(index);
        Team team = Team.instance();
        int price = (int)e.getPrice();
        if (team.getMoney() < price)
            return 1;
        team.reduceMoney(price);
        team.addEquipment(e);
        return 0;
    }
    */

    public int buyItem(Equipment equipment) {
        Team team = Team.instance();
        int price = (int)equipment.getPrice();
        if (team.getMoney() < price)
            return 1;
        team.reduceMoney(price);
        //team.addEquipment(equipment);
        shopItems.remove(equipment);
        purchasedItems.add(equipment);
        equipment.setPurchased(true);
        team.updateResources();
        return 0;
    }

    public int upgradeItem(Equipment equipment) {
        Team team = Team.instance();
        int price = (int)equipment.getPrice();
        if (team.getMoney() < price)
            return 1;
        team.reduceMoney(price);
        ((Upgradable) equipment).upgrade();
        team.updateResources();
        return 0;
    }


    public ArrayList<Equipment> getShopItemList() { return shopItems; }
    public ArrayList<Equipment> getPurchasedItemList() { return purchasedItems; }

}
