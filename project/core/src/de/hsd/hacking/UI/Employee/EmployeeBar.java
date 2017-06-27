package de.hsd.hacking.UI.Employee;

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

    private EmployeeProfile profilePopup;

    private int width = 160;
    private int originX;
    private int originY;

    public EmployeeBar() {
        super();

        originX = (int) GameStage.VIEWPORT_WIDTH - width;
        originY = (int) GameStage.VIEWPORT_HEIGHT - 20;

        profilePopup = new EmployeeProfile(new EmployeeProfile.EmployeeProvider() {
            @Override
            public Employee get() {
                return GetSelected();
            }
        });

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
                                          if (profilePopup.isActive()) {
                                              profilePopup.Close();
                                          } else {
                                              profilePopup.Show();
                                          }
                                      }
                                  }
        );
        detailsButton.setBounds(originX, originY - 60, width, 20);

        addActor(detailsButton);
        addActor(profilePopup);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (!Team.instance().isEmployeeSelected()) return;

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {

        if (!Team.instance().isEmployeeSelected()) return;

        nameLabel.setText("" + GetSelected().getName());
        jobLabel.setText("" + GetSelected().getState().getDisplayName());

        super.act(delta);
    }

    private Employee GetSelected() {
        return Team.instance().getSelectedEmployee();
    }
}
