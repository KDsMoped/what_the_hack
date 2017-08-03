package de.hsd.hacking.UI.Shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.EquipmentManager;
import de.hsd.hacking.Entities.Objects.Equipment.Upgradable;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.UI.General.TabbedView;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Constants;

public class ShopBrowser extends Popup {

    private Table shopContainer, upgradeContainer, finishedContainer;
    private EquipmentManager equipmentManager;

    public ShopBrowser() {
        super();
    }

    public void init() {
        this.equipmentManager = EquipmentManager.instance();

        initShopTable();

        equipmentManager.addRefreshEmployeeListener(new Callback() {
            @Override
            public void callback() {
                refreshList();
            }
        });

        ArrayList<Actor> views = new ArrayList<Actor>();
        views.add(initShopTable());
        views.add(initUpgradeTable());
        views.add(initFinishedTable());
        TabbedView tabbedView = new TabbedView(views);

        // Set tabbed view as main view
        this.addMainContent(tabbedView);
    }

    @Override
    public void act(float delta) {
        if (!isActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void show() {

        refreshList();

        super.show();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }


    private Table initShopTable() {
        Table content = initSubTable();
        content.setName("Shop");

        ScrollPane scroller = new ScrollPane(shopContainer = new Table());
        scroller.setStyle(Constants.ScrollPaneStyleWin32());
        scroller.setFadeScrollBars(false);

        content.row();
        content.add(scroller).expand().fill().prefHeight(SCROLLER_HEIGHT).maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH);
        return content;
    }

    private Table initUpgradeTable() {
        Table content = initSubTable();
        content.setName("Upgrades");

        ScrollPane scroller = new ScrollPane(upgradeContainer = new Table());
        scroller.setStyle(Constants.ScrollPaneStyleWin32());
        scroller.setFadeScrollBars(false);

        content.row();
        content.add(scroller).expand().fill().prefHeight(SCROLLER_HEIGHT).maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH);
        return content;
    }

    private Table initFinishedTable() {
        Table content = initSubTable();
        content.setName("Finished");

        ScrollPane scroller = new ScrollPane(finishedContainer = new Table());
        scroller.setStyle(Constants.ScrollPaneStyleWin32());
        scroller.setFadeScrollBars(false);

        content.row();
        content.add(scroller).expand().fill().prefHeight(SCROLLER_HEIGHT).maxHeight(SCROLLER_HEIGHT).prefWidth(SCROLLER_WIDTH).maxWidth(SCROLLER_WIDTH);
        return content;
    }

    private static Table initSubTable() {
        Table table = new Table();
        table.align(Align.top);
        table.setTouchable(Touchable.enabled);
        table.setBackground(Assets.instance().tab_view_border_patch);
        return table;
    }

    private void refreshList() {
        shopContainer.clearChildren();
        shopContainer.top();
        for (final Equipment equipment : equipmentManager.getShopItemList()) {

            shopContainer.add(new ShopUIElement(equipment)).expandX().fillX().padBottom(5).padRight(4).row();
        }

        upgradeContainer.clearChildren();
        upgradeContainer.top();
        finishedContainer.clearChildren();
        finishedContainer.top();
        for (final Equipment equipment : equipmentManager.getPurchasedItemList()) {
            if(equipment instanceof Upgradable && equipment.getLevel() < ((Upgradable) equipment).getMaxLevel()) {
                upgradeContainer.add(new ShopUIElement(equipment)).expandX().fillX().padBottom(5).padRight(4).row();
            }
            else finishedContainer.add(new ShopUIElement(equipment)).expandX().fillX().padBottom(5).padRight(4).row();
        }
    }

}
