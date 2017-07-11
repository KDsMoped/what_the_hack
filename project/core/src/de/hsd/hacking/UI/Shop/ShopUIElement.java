package de.hsd.hacking.UI.Shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;


import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by domin on 28.06.2017.
 */

public class ShopUIElement extends Table {

    Table content;

    Equipment equipment;
    EquipmentManager equipmentManager;
    ShopBrowser shopBrowser;

    private Label name;
    private Label price;
    private Label attribute;
    private Label level;

    TextButton buyButton;
    TextButton upgradeButton;

    public ShopUIElement(Equipment equipment) {
        this.equipment = equipment;
        this.equipmentManager = EquipmentManager.instance();
        this.shopBrowser = ShopBrowser.instance();

        InitControls();
        InitTable();
    }

    public void InitControls() {
        buyButton = new TextButton("Buy Item", Constants.TextButtonStyle());
        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(equipmentManager.buyItem(equipment) == 1) {}
                shopBrowser.updateTable();
            }
        });

        upgradeButton = new TextButton("Upgrade", Constants.TextButtonStyle());
        upgradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (equipmentManager.upgradeItem(equipment) == 1) {
                }
                shopBrowser.updateTable();
            }
        });
    }

    public void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);
        this.setBackground(Assets.instance().tab_view_border_patch);

        content = new Table();

        name = new Label(equipment.getName(), Constants.LabelStyle());
        price = new Label(Float.toString(equipment.getPrice()) + "$", Constants.LabelStyle());
        attribute = new Label("Möp", Constants.LabelStyle());
        //level = new Label("Lvl. " + Integer.toString(equipment.getLevel()), Constants.LabelStyle());

        content.add(name).expandX().fillX().left();
        content.add(price).right().padLeft(10);
        content.row();
        content.add(attribute).left().expand().fill();
        content.add(level).right().padLeft(5);
        content.row();

        this.add(content).expandX().fillX().left();
        if(equipmentManager.getPurchasedItemList().contains(equipment)) {
            if(equipment instanceof Upgradable) {
                this.add(upgradeButton).right().padLeft(10).size(100, 30);
            }
        }
        else {
            this.add(buyButton).right().padLeft(10).size(100, 30);
        }
    }

    public void updateTable() {
        content.clear();

        name = new Label(equipment.getName(), Constants.LabelStyle());
        price = new Label(Float.toString(equipment.getPrice()) + "$", Constants.LabelStyle());
        attribute = new Label("Möp", Constants.LabelStyle());
        level = new Label("Lvl. " + Integer.toString(equipment.getLevel()), Constants.LabelStyle());

        content.add(name).expandX().fillX().left();
        content.add(price).right().padLeft(10);
        content.row();
        content.add(attribute).left().expand().fill();
        content.add(level).right().padLeft(5);
        content.row();

        this.clear();
        this.add(content).expandX().fillX().left();
        this.add(buyButton).right().padLeft(10).size(100, 30);
    }
}
