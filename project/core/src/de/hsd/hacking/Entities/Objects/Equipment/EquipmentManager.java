package de.hsd.hacking.Entities.Objects.Equipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Entities.Objects.Equipment.Items.CoffeeMachine;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Items.HardwareStation;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Modem;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Router;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Server;
import de.hsd.hacking.Entities.Objects.Interactable;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Entities.Team.Workspace;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;


/**
 * Created by domin on 28.06.2017.
 */

public class EquipmentManager {

    private ArrayList<Equipment> shopItems = new ArrayList<Equipment>();
    private ArrayList<Equipment> purchasedItems = new ArrayList<Equipment>();

    private static EquipmentManager instance = null;

    private ArrayList<Callback> refreshEquipmentListener = new ArrayList<Callback>();

    private EquipmentManager() {
    }

    public void initBasicEquipment(){
        Load();

        List<Workspace> workspaces = GameStage.instance().getWorkspaces();

        if (!containsItem("Computer 1")) {
            Computer computer1 = new Computer("Computer 1", workspaces.get(0));
            shopItems.add(computer1);
            buyItem(computer1, false);
        }
        if (!containsItem("Computer 2")) {
            Computer computer2 = new Computer("Computer 2", workspaces.get(1));
            shopItems.add(computer2);
        }
        if (!containsItem("Computer 3")) {
            Computer computer3 = new Computer("Computer 3", workspaces.get(2));
            shopItems.add(computer3);
        }
        if (!containsItem("Computer 4")) {
            Computer computer4 = new Computer("Computer 4", workspaces.get(3));
            shopItems.add(computer4);
        }
        if (!containsInstance(purchasedItems, Modem.class)) {
            Modem modem = new Modem();
            shopItems.add(modem);
        }
        if (!containsInstance(purchasedItems, CoffeeMachine.class)) {
            CoffeeMachine coffeeMachine = new CoffeeMachine();
            shopItems.add(coffeeMachine);
        }
        if (!containsInstance(purchasedItems, Router.class)) {
            Router router = new Router();
            shopItems.add(router);
        }
        if (!containsInstance(purchasedItems, Server.class)) {
            Server server = new Server();
            shopItems.add(server);
        }
        if (!containsInstance(purchasedItems, HardwareStation.class)) {
            HardwareStation hardwareStation = new HardwareStation();
            shopItems.add(hardwareStation);
        }
    }

    public static EquipmentManager instance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    public int buyItem(Equipment equipment, Boolean pay) {
        Team team = Team.instance();
        if (pay) {
            int price = (int)equipment.getPrice();
            if (team.getMoney() < price)
                return 1;
            team.reduceMoney(price);
        }
//        Gdx.app.log(Constants.TAG, "buying " + equipment.getName() + " for " + price + "$.");

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

//        Gdx.app.log(Constants.TAG, "upgrading " + equipment.getName() + " for " + price + "$.");

        if(equipment.getLevel() >= ((Upgradable) equipment).getMaxLevel()) {
            finishItem(equipment);
        }

        notifyRefreshListeners();
        return 0;
    }

    private void finishItem(Equipment equipment) {


    }

    public Collection<Equipment> getShopItemList() { return Collections.unmodifiableList(shopItems); }
    public Collection<Equipment> getPurchasedItemList() { return Collections.unmodifiableList(purchasedItems); }


    public void addRefreshEmployeeListener(Callback callback) {
        if (!refreshEquipmentListener.contains(callback)) refreshEquipmentListener.add(callback);
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshEquipmentListener.toArray(new Callback[refreshEquipmentListener.size()])) {
            c.callback();
        }
    }

    /**
     * Save all the purchased equipment
     * @return Build Proto EquipmentManager object to write to disk.
     */
    public Proto.EquipmentManager Save() {
        Proto.EquipmentManager.Builder builder = Proto.EquipmentManager.newBuilder();

        for (Equipment equipment: purchasedItems) {
            builder.addEquipment(equipment.getData());
        }

        return builder.build();
    }

    /**
     * Restores the purchased equipment from a previous game.
     * @return True if equipment was loaded.
     */
    public Boolean Load() {
        Proto.EquipmentManager.Builder proto = SaveGameManager.getEquipmentManager();
        if (proto != null) {
            for (Proto.Equipment equipment : proto.getEquipmentList()) {
                switch (equipment.getType()) {
                    case CoffeeMachine:
                        CoffeeMachine coffee = new CoffeeMachine();
                        coffee.setLevel(equipment.getLevel());
                        coffee.setPrice(equipment.getPrice());
                        coffee.setPurchased(true);
                        purchasedItems.add(coffee);
                        break;
                    case Computer:
                        int number = Integer.parseInt(equipment.getName().substring(equipment.getName().length() - 1)) - 1;
                        Computer comp = new Computer(equipment.getName(), GameStage.instance().getWorkspaces().get(number));
                        comp.setLevel(equipment.getLevel());
                        comp.setPrice(equipment.getPrice());
                        comp.setPurchased(true);
                        purchasedItems.add(comp);
                        break;
                    case HardwareStation:
                        HardwareStation hw = new HardwareStation();
                        hw.setLevel(equipment.getLevel());
                        hw.setPrice(equipment.getPrice());
                        hw.setPurchased(true);
                        purchasedItems.add(hw);
                        break;
                    case Modem:
                        Modem modem = new Modem();
                        modem.setLevel(equipment.getLevel());
                        modem.setPrice(equipment.getPrice());
                        modem.setPurchased(true);
                        purchasedItems.add(modem);
                        break;
                    case Router:
                        Router router = new Router();
                        router.setLevel(equipment.getLevel());
                        router.setPrice(equipment.getPrice());
                        router.setPurchased(true);
                        purchasedItems.add(router);
                        break;
                    case Server:
                        Server server = new Server();
                        server.setLevel(equipment.getLevel());
                        server.setPrice(equipment.getPrice());
                        server.setPurchased(true);
                        purchasedItems.add(server);
                        break;
                }
            }

            Team.instance().updateResources();
            notifyRefreshListeners();

            return true;
        }
        return false;
    }

    public static <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsItem(String name) {
        for (Equipment e : purchasedItems) {
            if (e.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
