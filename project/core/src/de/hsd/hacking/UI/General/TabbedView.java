package de.hsd.hacking.UI.General;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 28.06.17.
 */

public class TabbedView extends Table {
    List<Actor> views;

    public TabbedView(List<String> tabs) {
        views = new ArrayList<Actor>();

        for (String s:tabs) {
            TextButton tabSwitcher = new TextButton(s, Constants.TabButtonStyle());
            this.add(tabSwitcher).padLeft(-2);
        }
    }

    public void AddTabContent(int element, Actor content) {
        views.set(element, content);
    }
}
