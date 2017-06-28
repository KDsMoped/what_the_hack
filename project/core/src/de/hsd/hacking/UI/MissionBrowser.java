package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.MissionFactory;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 22.06.17.
 */

public class MissionBrowser extends Popup {
    private VerticalGroup contentContainer;
    private Table content;

    private Label title;

    private Table missionContainer = new Table();
    private ScrollPane missionScroller;


    /**
     * We need the ui assets to display a beautiful popup window.
     */
    public MissionBrowser() {
        super();

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
        content.setDebug(true);

        title = new Label("Missions", Constants.LabelStyle());

        missionScroller = new ScrollPane(missionContainer);

        for (int i = 0; i < 5; i++) {
            missionContainer.add(new MissionUIElement(MissionFactory.CreateRandomMission())).height(50).expandX().fillX();
            missionContainer.row();
        }

        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5);
        content.row();
        content.add(missionScroller).expand().fill().maxHeight(185);
    }
}
