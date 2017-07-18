package de.hsd.hacking.Entities.Objects.Equipment;

import java.util.ArrayList;

import de.hsd.hacking.Entities.Objects.Equipment.Items.CoffeeMachine;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Modem;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Router;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Server;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Utils.Callback.Callback;

/**
 * Created by domin on 28.06.2017.
 */

public class EquipmentManager {

    private ArrayList<Equipment> shopItems = new ArrayList<Equipment>();
    private ArrayList<Equipment> purchasedItems = new ArrayList<Equipment>();

    private static EquipmentManager instance = null;

    private ArrayList<Callback> refreshEquipmentListener = new ArrayList<Callback>();

    private EquipmentManager() {
        Computer computer = new Computer();
        shopItems.add(computer);
        Modem modem = new Modem();
        shopItems.add(modem);
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        shopItems.add(coffeeMachine);
        Router router = new Router();
        shopItems.add(router);
        Server server = new Server();
        shopItems.add(server);
    }

    public static EquipmentManager instance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    public int buyItem(Equipment equipment) {
        Team team = Team.instance();
        int price = (int)equipment.getPrice();
        if (team.getMoney() < price)
            return 1;
        team.reduceMoney(price);

        shopItems.remove(equipment);
        purchasedItems.add(equipment);
        equipment.setPurchased(true);
        team.updateResources();

        notifyRefreshListeners();
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

        if(equipment.getLevel() >= ((Upgradable) equipment).getMaxLevel()) {
            finishItem(equipment);
        }

        notifyRefreshListeners();
        return 0;
    }

    private void finishItem(Equipment equipment) {


    }

    public ArrayList<Equipment> getShopItemList() { return shopItems; }
    public ArrayList<Equipment> getPurchasedItemList() { return purchasedItems; }


    public void addRefreshEmployeeListener(Callback callback) {
        if (!refreshEquipmentListener.contains(callback)) refreshEquipmentListener.add(callback);
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshEquipmentListener.toArray(new Callback[refreshEquipmentListener.size()])) {
            c.callback();
        }
    }

}
