package de.hsd.hacking.UI.Shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    Table leftContent;
    Table midContent;
    Table rightContent;

    Equipment equipment;
    EquipmentManager equipmentManager;
    ShopBrowser shopBrowser;

    private Label name;
    private Label price;
    private Label attribute;
    private Label level;

    private Image icon;

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
        this.setBackground(Assets.instance().table_border_patch);

        leftContent = new Table();
        midContent = new Table();
        rightContent = new Table();

        name = new Label(equipment.getName(), Constants.LabelStyle());
        price = new Label(Float.toString(equipment.getPrice()) + "$", Constants.LabelStyle());

        level = new Label("Lvl. " + Integer.toString(equipment.getLevel()), Constants.LabelStyle());

        icon = new Image(equipment.getIcon());
        Container<Image> iconContainer = new Container<Image>(icon);
        iconContainer.setBackground(Assets.instance().table_border_patch);

        leftContent.setWidth(30);
        leftContent.add(iconContainer);
        leftContent.row();
        leftContent.add(level).left().expand().fillX().padRight(10);

        midContent.setWidth(60);
        midContent.add(name).left().expandX().fillX();
        //midContent.add(price).right().padLeft(10);
        midContent.row();

        if(equipment.getBandwidthBonus() > 0) {
            midContent.add(new Label("Bandwidth +" + Integer.toString(equipment.getBandwidthBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getComputationPowerBonus() > 0) {
            midContent.add(new Label("ComputationPower +" + Integer.toString(equipment.getComputationPowerBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getAllPurposeSkillBonus() > 0) {
            midContent.add(new Label("All-Purpose +" + Integer.toString(equipment.getAllPurposeSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getSocialSkillBonus() > 0) {
            midContent.add(new Label("Social +" + Integer.toString(equipment.getSocialSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getCryptoSkillBonus() > 0) {
            midContent.add(new Label("Crypto +" + Integer.toString(equipment.getCryptoSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getHardwareSkillBonus() > 0) {
            midContent.add(new Label("Hardware +" + Integer.toString(equipment.getHardwareSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getSoftwareSkillBonus() > 0) {
            midContent.add(new Label("Software +" + Integer.toString(equipment.getSoftwareSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getNetworkSkillBonus() > 0) {
            midContent.add(new Label("Network +" + Integer.toString(equipment.getNetworkSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }
        if(equipment.getSearchSkillBonus() > 0) {
            midContent.add(new Label("Search +" + Integer.toString(equipment.getSearchSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            midContent.row();
        }

        rightContent.add(price).right().padLeft(10).padTop(5);
        rightContent.row();
        if(equipmentManager.getPurchasedItemList().contains(equipment)) {
            if(equipment instanceof Upgradable) {
                rightContent.add(upgradeButton).size(100, 30).right().padLeft(5).padBottom(5);
            }
        }
        else {
            rightContent.add(buyButton).size(100, 30).right().padLeft(5).padBottom(5);
        }

        this.add(leftContent).expandX().fillX().left();
        this.add(midContent).expandX().fillX().left();
        this.add(rightContent).right().padRight(10);

    }

    /*
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
    */
}
