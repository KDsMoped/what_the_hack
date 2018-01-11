package de.hsd.hacking.Entities.Objects.Equipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;

import de.hsd.hacking.Data.Manager;
import de.hsd.hacking.Data.ProtobufHandler;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Entities.Objects.Equipment.Items.CoffeeMachine;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Items.HardwareStation;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Modem;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Router;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Server;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.Entities.Team.Workspace;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Callback.Callback;


/**
 * Creates, stores and manages all {@link Equipment} objects, that can be purchased and upgraded.
 *
 * @author Dominik
 */

public class EquipmentManager implements Manager, ProtobufHandler {

    private ArrayList<Equipment> shopItems = new ArrayList<Equipment>();
    private ArrayList<Equipment> purchasedItems = new ArrayList<Equipment>();

    private static EquipmentManager instance = null;

    private ArrayList<Callback> refreshEquipmentListener = new ArrayList<Callback>();

    private EquipmentManager() {
    }

    /**
     * Creates an instance of this manager.
     */
    public static void createInstance() {
        if (instance != null) {
            Gdx.app.error("", "ERROR: Instance of EquipmentManager has not been destroyed before creating new one.");
            return;
        }

        instance = new EquipmentManager();
    }

    /**
     * Provides an instance of this manager;
     *
     * @return
     */
    public static EquipmentManager instance() {

        if (instance == null)
            Gdx.app.error("", "ERROR: Instance of EquipmentManager has not been created yet. Use createInstance() to do so.");

        return instance;
    }

    /**
     * Creates the purchasable Objects to list them in the Shop
     */
    public void initBasicEquipment() {
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

    /**
     * Purchases the specified Equipment and tells the {@link TeamManager} to update the resources.
     *
     * @param equipment the Item to buy
     * @param pay       whether to pay or not
     * @return 0 if succeeds
     */
    public int buyItem(Equipment equipment, Boolean pay) {
        TeamManager teamManager = TeamManager.instance();
        if (pay) {
            int price = (int) equipment.getPrice();
            if (teamManager.getMoney() < price)
                return 1;
            teamManager.reduceMoney(price);
        }
//        Gdx.app.log(Constants.TAG, "buying " + equipment.getName() + " for " + price + "$.");

        shopItems.remove(equipment);
        purchasedItems.add(equipment);
        equipment.onPurchase(true);
        teamManager.updateResources();

        notifyRefreshListeners();
        return 0;
    }

    /**
     * Upgrades the specified Equipment and tells the {@link TeamManager} to update the resources.
     *
     * @param equipment the Item to upgrade
     * @return 0 if succeeds
     */
    public int upgradeItem(Equipment equipment) {
        TeamManager teamManager = TeamManager.instance();
        int price = (int) equipment.getPrice();
        if (teamManager.getMoney() < price)
            return 1;
        teamManager.reduceMoney(price);
        ((Upgradable) equipment).upgrade();
        teamManager.updateResources();

//        Gdx.app.log(Constants.TAG, "upgrading " + equipment.getName() + " for " + price + "$.");

        notifyRefreshListeners();
        return 0;
    }


    public Collection<Equipment> getShopItemList() {
        return Collections.unmodifiableList(shopItems);
    }

    public Collection<Equipment> getPurchasedItemList() {
        return Collections.unmodifiableList(purchasedItems);
    }


    public void addRefreshEquipmentListener(Callback callback) {
        if (!refreshEquipmentListener.contains(callback)) refreshEquipmentListener.add(callback);
    }

    private void notifyRefreshListeners() {
        for (Callback c : refreshEquipmentListener.toArray(new Callback[refreshEquipmentListener.size()])) {
            c.callback();
        }
    }

    /**
     * Save all the purchased equipment
     *
     * @return Build Proto EquipmentManager object to write to disk.
     */
    public Proto.EquipmentManager Save() {
        Proto.EquipmentManager.Builder builder = Proto.EquipmentManager.newBuilder();

        for (Equipment equipment : purchasedItems) {
            builder.addEquipment(equipment.getData());
        }

        return builder.build();
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

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    @Override
    public void initReferences() {

    }

    /**
     * Creates the default state of this manager when a new game is started.
     */
    @Override
    public void loadDefaultState() {

    }

    /**
     * Recreates the state this manager had before serialization.
     */
    @Override
    public void loadState() {
        Proto.EquipmentManager.Builder proto = SaveGameManager.getEquipmentManager();

        for (Proto.Equipment equipment : proto.getEquipmentList()) {
            switch (equipment.getType()) {
                case CoffeeMachine:
                    CoffeeMachine coffee = new CoffeeMachine();
                    coffee.setLevel(equipment.getLevel());
                    coffee.setPrice(equipment.getPrice());
                    coffee.onPurchase(true);
                    purchasedItems.add(coffee);
                    break;
                case Computer:
                    Computer comp = new Computer(equipment.getName(), null);
                    comp.setLevel(equipment.getLevel());
                    comp.setPrice(equipment.getPrice());
                    comp.onPurchase(true);
                    purchasedItems.add(comp);
                    break;
                case HardwareStation:
                    HardwareStation hw = new HardwareStation();
                    hw.setLevel(equipment.getLevel());
                    hw.setPrice(equipment.getPrice());
                    hw.onPurchase(true);
                    purchasedItems.add(hw);
                    break;
                case Modem:
                    Modem modem = new Modem();
                    modem.setLevel(equipment.getLevel());
                    modem.setPrice(equipment.getPrice());
                    modem.onPurchase(true);
                    purchasedItems.add(modem);
                    break;
                case Router:
                    Router router = new Router();
                    router.setLevel(equipment.getLevel());
                    router.setPrice(equipment.getPrice());
                    router.onPurchase(true);
                    purchasedItems.add(router);
                    break;
                case Server:
                    Server server = new Server();
                    server.setLevel(equipment.getLevel());
                    server.setPrice(equipment.getPrice());
                    server.onPurchase(true);
                    purchasedItems.add(server);
                    break;
            }
        }

        TeamManager.instance().updateResources();
        notifyRefreshListeners();
    }

    public void placeExistingEquipment() {
        for (Equipment equipment : purchasedItems) {

            equipment.addToTileMap();

//            if (equipment instanceof Computer) {
//
//
//            }
        }
    }

    /**
     * Destroys manager this instance.
     */
    @Override
    public void cleanUp() {
        instance = null;
    }
}
