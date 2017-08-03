package de.hsd.hacking.UI.General;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.Locale;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.TimeChangedListener;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.DateUtils;

// TODO Vor dem Zahltag -15.000$ blinken lassen
public class StatusBar extends Actor implements TimeChangedListener {

    private Assets assets;

    // StatusBar items
    private int date = 1;
//    private float time = 0.0f;
    private int bandwidth = 0;
    private int employees = 0;
    private int workplaces = 0;
    private int money = 0;

    /**
     * Value that will be displayed on the next act and draw call. Might be different than the actual available resource.
     */
    private int displayedMoney = money;
    private int displayedBandwidth = bandwidth;
    /**
     * Time between 0-STATUS_BAR_ANIMATION_TIME that is elapsed between the old and the new value while an animation is happening.
     */
    private float elapsedMoney = 0;
    private float elapsedBandwidth= 0;
    /**
     * Previous value of the ressource variable.
     */
    private int oldMoney = money;
    private int oldBandwidth = bandwidth;

    private Image timeLabel;

    // label objects that will be added to the table.
    private Label moneyText;
    private Label bandwidthText;
    private Label dateText;
    private Label employeesText;

    // the actual top bar
    private Table items;

    /**
     * Initializes a new top bar. Top bar needs assets to get the ui themes and fonts.
     */
    public StatusBar() {
        this.assets = Assets.instance();

        // this is the actual parent object
        items = new Table();
        // align content in cells
        items.align(Align.center);
//        items.setWidth(GameStage.VIEWPORT_WIDTH * (2.0f / 3.0f));
        items.setWidth(312);
        int STATUS_BAR_HEIGHT = 20;
        items.setHeight(STATUS_BAR_HEIGHT);
        // the green terminal background style
        items.setBackground(assets.terminal_patch);

        // font style for the text, use monospace!
        Label.LabelStyle titlebarStyle = Constants.TerminalLabelStyle();

        // labels with sprites
        Image moneyLabel = new Image(assets.money_icon, Scaling.none, Align.top);
        Image bandwidthLabel = new Image(assets.bandwith_icon, Scaling.none, Align.bottom);
        Image employeesLabel = new Image(assets.employees_icon, Scaling.none, Align.bottom);
        timeLabel = new Image(assets.clock_icon.first(), Scaling.none, Align.bottom);

        // set font to label objects
        moneyText = new Label("", titlebarStyle);
        bandwidthText = new Label("", titlebarStyle);
        dateText = new Label("", titlebarStyle);
        employeesText = new Label("", titlebarStyle);

        // horizontal spacing between the items
        items.add(moneyLabel).padLeft(0);
        items.add(moneyLabel).padRight(1);
        items.add(moneyText).padRight(8);
        items.add(bandwidthLabel).padRight(4);
        items.add(bandwidthText).padRight(6);
        items.add(employeesLabel);
        items.add(employeesText).padRight(60);
        items.add(dateText).align(Align.right).padRight(15);
        items.add(timeLabel).align(Align.right).padRight(6);


        // we want to center the top bar, to calculate the x position can
        // window_width / 2 - topbar_width / 2
        // so we have to set position as last because we need the width of the top bar
        items.setPosition((Constants.VIEWPORT_WIDTH / 2) - (items.getWidth() / 2), Constants.VIEWPORT_HEIGHT - items.getHeight() + 0);

        this.setDate(GameTime.instance.getCurrentDay());
        this.setTime(GameTime.instance.getCurrentStep());
    }

    /**
     * Refreshes the displayed values
     * @param delta
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        if (displayedBandwidth != bandwidth){
            elapsedBandwidth += delta;
            displayedBandwidth = AnimateIntChange(bandwidth, oldBandwidth, elapsedBandwidth);
        }

        if (displayedMoney != money) {
            elapsedMoney += delta;
            displayedMoney = AnimateIntChange(money, oldMoney, elapsedMoney);
        }

        // set the text for money etc. in the label objects
        moneyText.setText(String.format(Locale.GERMAN, "%05d", displayedMoney));
        bandwidthText.setText(String.format(Locale.GERMAN, "%04d", displayedBandwidth));
        employeesText.setText(Integer.toString(employees) + "/" + Constants.MAX_EMPLOYEE_COUNT);
        dateText.setText(Constants.dateFormatBar.format(DateUtils.ConvertDaysToDate(date)));
    }

    /**
     * Draw the top bar.
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        items.draw(batch, parentAlpha);
    }

    /**
     * Calculates an animation between two values.
     * @param newValue new value of variable
     * @param oldValue old value of variable
     * @param elapsed elapsed time since new value
     * @return
     */
    private int AnimateIntChange(int newValue, int oldValue, float elapsed) {
        int animatedValue;

        int STATUS_BAR_ANIMATION_TIME = 1;
        float progress = Math.min(1.0f, elapsed / STATUS_BAR_ANIMATION_TIME);
        float interpol = Interpolation.sineOut.apply(progress);

        animatedValue = (int)((newValue - oldValue) * interpol);

        return animatedValue + oldValue;
    }

//    public float getTime() {
//        return time;
//    }

    /**
     * set the time an display it as an circle
     * @param step time between 0-8
     */
    public void setTime(int step) {
        if (step < 0 || step > 8){
            throw new IllegalArgumentException("setTime(step) called with step value: '" + step + "' . Should be between 0 and 8 inclusively.");
        }
        timeLabel.setDrawable(assets.clock_icon.get(step));
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getBandwith() {
        return bandwidth;
    }

    public void setBandwith(int bandwidth) {
        if (bandwidth == this.bandwidth)
            return;

        oldBandwidth = this.bandwidth;
        this.bandwidth = bandwidth;
        elapsedBandwidth = 0;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        if (employees == this.employees)
            return;
        this.employees = employees;
    }

    public int getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(int workplaces) {
        if (workplaces == this.workplaces)
            return;
        this.workplaces = workplaces;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        if (money == this.money)
            return;

        oldMoney = this.money;
        this.money = money;
        elapsedMoney = 0;
    }

    @Override
    public void timeChanged(float time) {
        //stub

    }

    @Override
    public void timeStepChanged(int step) {
        setTime(step);
    }

    @Override
    public void dayChanged(int days) {
        setDate(days);
    }

    @Override
    public void weekChanged(int week) {

    }
}
