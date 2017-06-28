package de.hsd.hacking.UI.Shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;


import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Objects.Equipment.Equipment;
import de.hsd.hacking.UI.Button;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by domin on 28.06.2017.
 */

public class ShopUIElement extends Table {

    Table content;

    Equipment equipment;

    private Label name;
    private Label price;
    private Label attributeType;
    private Label attributeValue;

    TextButton buyButton;

    public ShopUIElement(Equipment equipment) {
        this.equipment = equipment;

        InitControls();
        InitTable();
    }

    public void InitControls() {
        buyButton = new TextButton("Buy Item", Constants.TextButtonStyle());
        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    public void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);

        content = new Table();

        name = new Label(equipment.getName(), Constants.LabelStyle());
        price = new Label(Float.toString(equipment.getPrice()) + "$", Constants.LabelStyle());
        attributeType = new Label(equipment.getAttributeType().toString(), Constants.LabelStyle());
        attributeValue = new Label("+" + Integer.toString(equipment.getAttributeValue()), Constants.LabelStyle());

        content.add(name).expandX().fillX().left();
        content.add(price).right().padLeft(10);
        content.row();
        content.add(attributeType).left().expand().fill();
        content.add(attributeValue).right().padLeft(5);
        content.row();

        this.add(content).expandX().fillX().left();
        this.add(buyButton).right().padLeft(10);

    }
}
