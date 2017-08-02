package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Team.Team;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;

public class EmployeeBar extends Group {

    private Label nameLabel;

    private EmployeeProfile profilePopup;

    public EmployeeBar() {
        super();

        InitUI();
    }

    private void InitUI() {
        int width = 200;
        int originX = (int) GameStage.VIEWPORT_WIDTH - width - 10;
        int originY = (int) GameStage.VIEWPORT_HEIGHT - 90;

        Table table = new Table();
        table.setBackground(Assets.instance().win32_patch);
        table.setBounds(originX, originY, width, 60);
        //        table.setTouchable(Touchable.enabled);
        addActor(table);


        nameLabel = new Label("Name of Employee", Constants.LabelStyle());
        nameLabel.setAlignment(Align.center);
//        nameLabel.setBounds(originX, originY - 20, width, 20);
//        addActor(nameLabel);
        table.add(nameLabel).fill().expand().pad(2).center().row();

//        jobLabel = new Label("Job of Employee", Constants.LabelStyle());
//        jobLabel.setBounds(originX, originY - 40, width, 20);
//        addActor(jobLabel);

        Table buttonRow = new Table();
        table.add(buttonRow).fill().expand().pad(2, 30, 2, 30).row();

        TextButton detailsButton = new TextButton("Details", Constants.TextButtonStyle());
        detailsButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {
                                          onDetailsClick();
                                          AudioManager.instance().playUIButtonSound();
                                      }
                                  });

//        detailsButton.setBounds(originX, originY - 60, width, 20);
//        addActor(detailsButton);
//        table.add(detailsButton).fill().expand().pad(2, 30, 2, 4);
        buttonRow.add(detailsButton).fill().expand().padRight(8);

        TextButton deselectButton = new TextButton("X", Constants.TextButtonStyle());
        deselectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {onDeselectClick();
            }
        });

//        detailsButton.setBounds(originX, originY - 60, width, 20);
//        addActor(detailsButton);
//        table.add(deselectButton).fill().expand().pad(2, 4, 2, 30).row();
        buttonRow.add(deselectButton).fill().expand();


        addActor(profilePopup = new EmployeeProfile(new EmployeeProvider() {
            @Override
            public Employee get() {
                return GetSelected();
            }
        }));
    }

    private void onDetailsClick(){
        profilePopup.toggleView();
    }

    private void onDeselectClick(){
        Team.instance().deselectEmployee();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!Team.instance().isEmployeeSelected()
                || Team.instance().getSelectedEmployee().getCurrentMission() != null) return;

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {

        if (!Team.instance().isEmployeeSelected()
                || Team.instance().getSelectedEmployee().getCurrentMission() != null) return;

        nameLabel.setText("" + GetSelected().getName());
//        jobLabel.setText("" + GetSelected().getState().getDisplayName());

        super.act(delta);
    }

    private Employee GetSelected() {
        return Team.instance().getSelectedEmployee();
    }

    public boolean isEmployeeProfileOpen(){
        return profilePopup.isActive();
    }

    public void closeEmployeeProfile(){
        profilePopup.close();
    }
}
