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

/**
 * Created by ju on 22.06.17.
 */

public class MissionBrowser extends Popup {
    private VerticalGroup contentContainer;
    private Table content;

    private Label title;

    private Table missionContainer = new Table();
    private ScrollPane missionScroller;


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

        title = new Label("Missions", this.getLabelStyle());
        title.setFontScale(1.2f);

        missionScroller = new ScrollPane(missionContainer);

        for (int i = 0; i < 5; i++) {
            missionContainer.add(new MissionUIElement(MissionFactory.CreateRandomMission()))
                    .expandX().fillX().padTop(5).padBottom(5);
            missionContainer.row();
        }

        contentContainer.addActor(content);
        content.add(title).expandX().fillX().padTop(5).center();
        content.row();
        content.add(missionScroller).expand().fill().maxHeight(185);
    }
}
