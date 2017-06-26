package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Mission;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 22.06.17.
 */

public class MissionUIElement extends Table {
    private Mission mission;

    private Label name;
    private Label time;

    public MissionUIElement(Mission mission) {
        this.mission = mission;

        InitTable();
    }

    private void InitTable() {
        this.setTouchable(Touchable.enabled);
        this.align(Align.top);

        name = new Label(mission.getName(), Constants.LabelStyle());
        time = new Label(Integer.toString(mission.getDuration()), Constants.LabelStyle());

        this.add(name).expandX().fillX().left();
        this.add(time).right().padLeft(10);
    }
}
