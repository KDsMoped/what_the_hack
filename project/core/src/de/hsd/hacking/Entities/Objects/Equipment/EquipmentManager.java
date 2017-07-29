package de.hsd.hacking.Entities.Objects.Equipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import de.hsd.hacking.Entities.Objects.Equipment.Items.CoffeeMachine;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Computer;
import de.hsd.hacking.Entities.Objects.Equipment.Items.HardwareStation;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Modem;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Router;
import de.hsd.hacking.Entities.Objects.Equipment.Items.Server;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Entities.Team.Workspace;
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

        List<Workspace> workspaces = GameStage.instance().getWorkspaces();

        Computer computer1 = new Computer("Computer 1", workspaces.get(0));
        shopItems.add(computer1);
        buyItem(computer1);
        Computer computer2 = new Computer("Computer 2", workspaces.get(1));
        shopItems.add(computer2);
        Computer computer3 = new Computer("Computer 3", workspaces.get(2));
        shopItems.add(computer3);
        Computer computer4 = new Computer("Computer 4", workspaces.get(3));
        shopItems.add(computer4);
        Modem modem = new Modem();
        shopItems.add(modem);
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        shopItems.add(coffeeMachine);
        Router router = new Router();
        shopItems.add(router);
        Server server = new Server();
        shopItems.add(server);
        HardwareStation hardwareStation = new HardwareStation();
        shopItems.add(hardwareStation);
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

}
