package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.UI.General.TabbedView;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.Callback.EmployeeCallback;

import java.util.ArrayList;

public class EmployeeBrowser extends Popup {

//    private Table scrollContainer = new Table();

    private Table openEmployeeContainer, hiredEmployeeContainer;

    public EmployeeBrowser() {
        super();

        initOpenEmployees();

        EmployeeManager.instance().addRefreshEmployeeListener(new Callback() {
            @Override
            public void callback() {
                refreshList();
            }
        });

        ArrayList<Actor> views = new ArrayList<Actor>();
        views.add(initHiredEmployees());
        views.add(initOpenEmployees());
        TabbedView tabbedView = new TabbedView(views);

        // Set tabbed view as main view
        this.addMainContent(tabbedView);
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

    private Table initOpenEmployees() {
        Table content = initSubTable();
        content.setName("Hire");

        ScrollPane scroller = new ScrollPane(openEmployeeContainer = new Table());

        content.row();
        content.add(scroller).expand().fill().maxHeight(175).prefWidth(400).maxWidth(400);
        return content;
    }

    private Table initHiredEmployees() {
        Table content = initSubTable();
        content.setName("Team");

        ScrollPane scroller = new ScrollPane(hiredEmployeeContainer = new Table());

        content.row();
        content.add(scroller).expand().fill().maxHeight(175).prefWidth(400).maxWidth(400);
        return content;
    }

    private static Table initSubTable() {
        Table table = new Table();
        table.align(Align.top);
        table.setTouchable(Touchable.enabled);
        table.setBackground(Assets.instance().tab_view_border_patch);
        return table;
    }

//    private void onEmploy(Employee employee) {
//        EmployeeManager.instance().employ(employee);
//    }

    private void refreshList() {
        openEmployeeContainer.clearChildren();

        for (final Employee employee : EmployeeManager.instance().getAvailableEmployees()) {

            openEmployeeContainer.add(new EmployeeUIElement(employee)).expandX().fillX().padBottom(5).maxWidth(400).row();
        }

        hiredEmployeeContainer.clearChildren();

        for (final Employee employee : EmployeeManager.instance().getHiredEmployees()) {

            hiredEmployeeContainer.add(new EmployeeUIElement(employee)).expandX().fillX().padBottom(5).maxWidth(400).row();
        }
    }
}
