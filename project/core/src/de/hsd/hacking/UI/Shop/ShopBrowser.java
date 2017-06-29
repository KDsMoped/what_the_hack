package de.hsd.hacking.UI.Shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.Entities.Objects.Equipment.Shop;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by domin on 28.06.2017.
 */

public class ShopBrowser extends Popup {

    private VerticalGroup contentContainer;
    private Table content;

    private Label title;

    private Table itemContainer = new Table();
    private ScrollPane itemScroller;

    private Shop shop;


    /**
     * We need the ui assets to display a beautiful popup window.
     */
    public ShopBrowser() {
        super();

        this.shop = Shop.instance();

        InitTable();
    }

    @Override
    public void act(float delta) {
        if (!isActive()) {
            return;
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive()) {
            return;
        }

        super.draw(batch, parentAlpha);
    }


    private void InitTable() {
        contentContainer = this.getContent();

        content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);

        title = new Label("Shop", Constants.LabelStyle());
        title.setFontScale(1.3f);

        itemScroller = new ScrollPane(itemContainer);

        for(Equipment equipment : shop.getShopItemList()) {
            itemContainer.add(new ShopUIElement(equipment))
                    .expandX().fillX().padBottom(15);
            itemContainer.row();
        }

        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5);
        content.row();
        content.add(itemScroller).expand().fill().maxHeight(185);
    }
}
