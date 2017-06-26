package de.hsd.hacking.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

public class EmployeeBar extends Group {

    private Label nameLabel;
    private Label jobLabel;
    private TextButton detailsButton;

    private int width = 80;
    private int originX;
    private int originY;

    public EmployeeBar() {
        super();

        originX = (int) GameStage.VIEWPORT_WIDTH - 80;
        originY = (int) GameStage.VIEWPORT_HEIGHT;

        nameLabel = new Label("Name of Employee", Constants.LabelStyle());
        nameLabel.setBounds(originX, originY - 20, width, 20);
        addActor(nameLabel);

        jobLabel = new Label("Job of Employee", Constants.LabelStyle());
        jobLabel.setBounds(originX, originY - 40, width, 20);
        addActor(jobLabel);

        detailsButton = new TextButton("Details", Constants.TextButtonStyle());
        detailsButton.addListener(new ChangeListener() {
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
        detailsButton.setBounds(originX, GameStage.VIEWPORT_HEIGHT - 60, width, 20);

        addActor(detailsButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (!Team.getInstance().isEmployeeSelected()) return;

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {

        if (!Team.getInstance().isEmployeeSelected()) return;

        nameLabel.setText("Name: " + GetSelected().getName());
        jobLabel.setText("Doing: " + GetSelected().getState());

        super.act(delta);
    }

    private Employee GetSelected() {
        return Team.getInstance().getSelectedEmployee();
    }
}
