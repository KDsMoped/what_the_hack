package de.hsd.hacking.UI.General;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 28.06.17.
 */

/**
 * An ui element that is able to switch content between tabs.
 */
public class TabbedView extends Table {
    List<Actor> views;
    List<Button> buttons;
    VerticalGroup activeTab;
    Boolean userAction = false;

    /**
     * The only thing we need is a list of all the actors. Don't forget to set the name for each actor!
     * @param tabs List of tabs.
     */
    public TabbedView(List<Actor> tabs) {
        InitTable();

        views = tabs;
        buttons = new ArrayList<Button>();

        Table buttonHolder = new Table();

        for (Actor a:views) {
            TextButton tabSwitcher = new TextButton(" " + a.getName() + " ", Constants.TabButtonStyle());
            buttons.add(tabSwitcher);
            tabSwitcher.setName(a.getName());
            tabSwitcher.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    SwitchTab(actor);
                }
            });

            buttonHolder.add(tabSwitcher).padLeft(-1).height(20f);
        }

        this.add(buttonHolder).left().padTop(2f).padLeft(3f);
        this.row();
        this.add(activeTab);

        buttons.get(0).setChecked(true);
    }

    /**
     * Switch the tab view
     * @param sender Button that sended the click event
     */
    public void SwitchTab(Actor sender) {
        if (!userAction)
            userAction = true;
        else
            return;

        for (Actor a:views) {
            if (a.getName() == sender.getName()) {
                activeTab.addActor(a);
            }
            else {
                activeTab.removeActor(activeTab.findActor(a.getName()));
            }
        }

        for (Button b:buttons) {
            if (b.getName() == sender.getName()) {
                b.setChecked(true);
            }
            else {
                b.setChecked(false);
            }
        }

        userAction = false;
    }

    private void InitTable() {
        activeTab = new VerticalGroup();
    }
}
