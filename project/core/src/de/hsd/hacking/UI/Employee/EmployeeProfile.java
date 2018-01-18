package de.hsd.hacking.UI.Employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.Collection;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Entities.Employees.Employee;
import de.hsd.hacking.Entities.Employees.EmployeeManager;
import de.hsd.hacking.Entities.Employees.EmployeeSpecials.EmployeeSpecial;
import de.hsd.hacking.Entities.Employees.Skill;
import de.hsd.hacking.Entities.Team.TeamManager;
import de.hsd.hacking.UI.General.AudioTextButton;
import de.hsd.hacking.UI.General.DoubleLabelElement;
import de.hsd.hacking.UI.General.Popup;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Provider.EmployeeProvider;
import de.hsd.hacking.Utils.Provider.StringProvider;

/**
 * A popup that displays the values of an {@link Employee}.
 *
 * @author Hendrik
 */
public class EmployeeProfile extends Popup {

    private static final int TABLE_SPACING = 20;

    private EmployeeUIElement profilePopup;
    private EmployeeProvider employee;

    public EmployeeProfile(EmployeeProvider employee) {
        super(40);

        this.employee = employee;
        mainTable.setTouchable(Touchable.enabled);
//        mainTable.setDebug(true);

        initTable();
}

    private void initTable() {
        setAlignment(Align.center);
    }

    @Override
    public void show() {
        super.show();

        removeMainContent(profilePopup);
        addMainContent(profilePopup = new EmployeeUIElement(employee.get()));
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

        if (employee.get() == null) {
            return;
        }

        super.draw(batch, parentAlpha);
    }

    private Employee GetSelected() {
        return TeamManager.instance().getSelectedEmployee();
    }
}
