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
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.Proto;
import de.hsd.hacking.UI.General.AudioTextButton;
import de.hsd.hacking.Utils.Constants;

/**
 * This class is an ui element that displays an {@link Equipment} item with it's properties and buttons.
 * @author Dominik
 */

public class ShopUIElement extends Table {

    Table leftContent;
    Table midContent;
    Table rightContent;

    Equipment equipment;
    EquipmentManager equipmentManager;
    ShopBrowser shopBrowser;

    private Label level;

    AudioTextButton buyButton;
    AudioTextButton upgradeButton;

    public ShopUIElement(Equipment equipment) {
        this.equipment = equipment;
        this.equipmentManager = EquipmentManager.instance();
        this.shopBrowser = ShopBrowser.instance();

        InitControls();
        InitTable();
    }

    /**
     * Initializes the Buttons to buy or upgrade an Item.
     */
    public void InitControls() {
        buyButton = new AudioTextButton("Buy", Constants.TextButtonStyle());
        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(equipmentManager.buyItem(equipment, true) == 1) {}
            }
        });

        upgradeButton = new AudioTextButton("Upgrade", Constants.TextButtonStyle());
        upgradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (equipmentManager.upgradeItem(equipment) == 1) {}
            }
        });
    }

    /**
     * Initializes a set of Tables, containing Item information like name, bonuses, and price. Also
     * adds the respective button to buy or upgrade.
     */
    public void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);
        this.setBackground(Assets.instance().table_border_patch);
        pad(4f);

        //setDebug(true);

        leftContent = new Table();
        midContent = new Table();
        rightContent = new Table();

        Label name = new Label(equipment.getName(), Constants.LabelStyle());

        Image icon = new Image(equipment.getIcon());
        Container<Image> iconContainer = new Container<Image>(icon);
        iconContainer.setBackground(Assets.instance().table_border_patch);

        leftContent.add(iconContainer).top().left().width(32).height(40).padRight(10);
        leftContent.row();
        leftContent.add(level).left().expand().fillX().padTop(5);

        name.setFontScale(1.05f);
        midContent.add(name).top().left().expandX().fillX().padBottom(5);
        midContent.row();

        Table skillContent = new Table();

        if(equipment.getBandwidthBonus() > 0) {
            skillContent.add(new Image(Assets.instance().bandwith_icon)).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Bandwidth +" + Integer.toString(equipment.getBandwidthBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getComputationPowerBonus() > 0) {
            skillContent.add(new Image(Assets.instance().computer_icon)).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("ComputationPower +" + Integer.toString(equipment.getComputationPowerBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getAllPurposeSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.All_Purpose))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("All-Purpose +" + Integer.toString(equipment.getAllPurposeSkillBonus()), Constants.LabelStyle())).left().expandX().fillX();
            skillContent.row();
        }
        if(equipment.getSocialSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Social))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Social +" + Integer.toString(equipment.getSocialSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getCryptoSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Crypto))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Crypto +" + Integer.toString(equipment.getCryptoSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getHardwareSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Hardware))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Hardware +" + Integer.toString(equipment.getHardwareSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getSoftwareSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Software))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Software +" + Integer.toString(equipment.getSoftwareSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getNetworkSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Network))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Network +" + Integer.toString(equipment.getNetworkSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }
        if(equipment.getSearchSkillBonus() > 0) {
            skillContent.add(new Image(Assets.instance().getSkillIcon(Proto.SkillType.Search))).left().prefSize(13).maxWidth(13).minWidth(13).prefWidth(13).padRight(5);
            skillContent.add(new Label("Search +" + Integer.toString(equipment.getSearchSkillBonus()), Constants.LabelStyle())).left().expand().fill();
            skillContent.row();
        }

        midContent.add(skillContent).left();

        Label price = new Label(Float.toString(equipment.getPrice()) + "$", Constants.LabelStyle());
        level = new Label("Lvl: " + Integer.toString(equipment.getLevel()), Constants.LabelStyle());


        if(equipmentManager.getShopItemList().contains(equipment)) {
            rightContent.add(price).right().padTop(5).padBottom(2);
            rightContent.row();
            rightContent.add(buyButton).width(70).right().padBottom(5);
        }
        else {
            rightContent.add(level).right().top();
            if(equipment instanceof Upgradable && equipment.getLevel() < ((Upgradable) equipment).getMaxLevel()) {

                rightContent.row();
                rightContent.add(price).right().padTop(5).padBottom(2);
                rightContent.row();
                rightContent.add(upgradeButton).width(80).right().padBottom(5);
            }
        }

        this.add(leftContent).expandX().fillX().top().left().width(42);
        this.add(midContent).expandX().fillX().top().left().width(250);
        this.add(rightContent).expandX().fillX().right().width(100);

    }

}
