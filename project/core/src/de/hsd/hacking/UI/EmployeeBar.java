package de.hsd.hacking.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Utils.Constants;

public class EmployeeBar extends Group {

    public EmployeeBar() {
        super();

        TextButton button = new TextButton("Popup", Constants.UiSkin());
        button.addListener(new ChangeListener() {
                               @Override
                               public void changed(ChangeEvent event, Actor actor) {
//                                   if (popup.isActive()) {
//                                       popup.Close();
//                                       paused = false;
//                                   } else {
//                                       popup.Show();
//                                       paused = true;
//                                   }
                                   Gdx.app.log(Constants.TAG, "Button clicked!");
                               }
                           }
        );
        button.setBounds(10, 10, 100, 20);

        addActor(button);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Team.getInstance().isEmployeeSelected()) super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        if (Team.getInstance().isEmployeeSelected()) super.act(delta);
    }
}
