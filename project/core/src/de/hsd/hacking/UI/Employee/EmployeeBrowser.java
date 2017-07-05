package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.UI.General.TabbedView;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Callback.EmployeeCallback;
import de.hsd.hacking.Utils.Constants;

import java.util.ArrayList;

public class EmployeeBrowser extends Popup {

    private Table content;

    private Label title;

    private Table scrollContainer = new Table();
    private ScrollPane employeeScroller;


    public EmployeeBrowser() {
        super();

        InitTable();

        EmployeeManager.instance().addRefreshEmployeeListener(new Callback() {
            @Override
            public void callback() {
                refreshList();
            }
        });

//        ArrayList<Actor> views = new ArrayList<Actor>();
//        views.add(activeMissions);
//        views.add(openMissions);
//        TabbedView tabbedView = new TabbedView(views);
//
//        // Set tabbed view as main view
//        this.addMainContent(tabbedView);
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

    @Override
    public void show() {

        refreshList();

        super.show();
    }

    private void InitTable() {
        content = new Table();
        content.align(Align.top);
        content.setTouchable(Touchable.enabled);

        title = new Label("Recruitment", Constants.LabelStyle());
        title.setFontScale(1.2f);

        employeeScroller = new ScrollPane(scrollContainer);

        this.addMainContent(content);
        content.add(title).expandX().fillX().padTop(5).padBottom(5).center();
        content.row();
        content.add(employeeScroller).expand().fill().maxHeight(175).prefWidth(400).maxWidth(400);
    }

    private void onEmploy(Employee employee) {
        if (EmployeeManager.instance().employ(employee) == 0) return;

        refreshList();
    }

    private void refreshList() {
        scrollContainer.clearChildren();

        for (final Employee employee : EmployeeManager.instance().getAvailableEmployees()) {

            scrollContainer.add(new EmployeeUIElement(employee, new EmployeeCallback() {
                @Override
                public void callback(Employee employee) {
                    onEmploy(employee);
                }
            })).expandX().fillX().padBottom(5).maxWidth(400).row();
        }
    }
}
