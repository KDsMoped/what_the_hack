package de.hsd.hacking.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 05.06.17.
 */

public class StatusBar extends Actor {
    // Constants
    private final int STATUS_BAR_HEIGHT = 14;
    private final int STATUS_BAR_ANIMATION_TIME = 2;

    private Assets assets;

    // StatusBar items
    private int date = 1;
    private float time = 0.0f;
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

    // sprite objects that will be added to the table.
    private Image moneyLabel;
    private Image bandwidthLabel;
    private Image employeesLabel;
    private Image timeLabel;

    // Label style with font
    private Label.LabelStyle titlebarStyle;

    // label objects that will be added to the table.
    private Label moneyText;
    private Label bandwidthText;
    private Label dateText;
    private Label employeesText;

    // the actual top bar
    private Table items;

    /**
     * The date format we want for the top bar.
     */
    private final DateFormat df = new SimpleDateFormat("dd MMM");

    /**
     * Initializes a new top bar. Top bar needs assets to get the ui themes and fonts.
     * @param assets ui assets
     */
    public StatusBar(Assets assets) {
        this.assets = assets;

        // this is the actual parent object
        items = new Table();
        // align content in cells
        items.align(Align.center);
        items.setWidth(GameStage.VIEWPORT_WIDTH * (2.0f / 3.0f));
        items.setHeight(STATUS_BAR_HEIGHT + 6);
        // the green terminal background style
        items.setBackground(assets.terminal_patch);

        // font style for the text, use monospace!
        titlebarStyle = new Label.LabelStyle();
        titlebarStyle.font = assets.status_bar_font;
        titlebarStyle.fontColor = new Color(41f/255f, 230f/255f, 41f/255f, 1f);

        // labels with sprites
        moneyLabel = new Image(assets.money_icon, Scaling.none, Align.top);
        bandwidthLabel = new Image(assets.bandwith_icon, Scaling.none, Align.bottom);
        employeesLabel = new Image(assets.employees_icon, Scaling.none, Align.bottom);
        timeLabel = new Image(assets.clock_icon.first(), Scaling.none, Align.bottom);

        // set font to label objects
        moneyText = new Label("", titlebarStyle);
        bandwidthText = new Label("", titlebarStyle);
        dateText = new Label("", titlebarStyle);
        employeesText = new Label("", titlebarStyle);

        // horizontal spacing between the items
        items.add(moneyLabel).padRight(1);
        items.add(moneyText).padRight(8);
        items.add(bandwidthLabel).padRight(4);
        items.add(bandwidthText).padRight(6);
        items.add(employeesLabel);
        items.add(employeesText).padRight(60);
        items.add(dateText).align(Align.right).padRight(15);
        items.add(timeLabel).align(Align.right).padRight(2);


        // we want to center the top bar, to calculate the x position can
        // window_width / 2 - topbar_width / 2
        // so we have to set position as last because we need the width of the top bar
        items.setPosition((GameStage.VIEWPORT_WIDTH / 2) - (items.getWidth() / 2), GameStage.VIEWPORT_HEIGHT - items.getHeight() + 1);
    }

    /**
     * Refreshes the displayed values
     * @param delta
     */
    @Override
    public void act(float delta) {
        // always call super first!
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
        employeesText.setText(Integer.toString(employees) + "/" + Integer.toString(workplaces));
        dateText.setText(df.format(ConvertDaysToDate(date)));
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

        float progress = Math.min(1.0f, elapsed / STATUS_BAR_ANIMATION_TIME);
        float interpol = Interpolation.sineOut.apply(progress);

        animatedValue = (int)((newValue - oldValue) * interpol);

        return animatedValue + oldValue;
    }

    /**
     * converts days starting from 1 to an date object starting 1.1.
     * @param days number in days starting with 1
     * @return
     */
    private Date ConvertDaysToDate(int days) {
        Date date = new Date();

        try {
            date = new SimpleDateFormat("D").parse(String.valueOf(days));
        }
        catch (Exception e) {
            Gdx.app.log(Constants.TAG, e.getMessage());
        }

        return date;
    }

    public float getTime() {
        return time;
    }

    /**
     * set the time an display it as an circle
     * @param time time between 0-1
     */
    public void setTime(float time) {
        this.time = time;

        if (time < 0.1f) {
            timeLabel.setDrawable(assets.clock_icon.first());
        }
        else if (time < 0.2f) {
            timeLabel.setDrawable(assets.clock_icon.get(1));
        }
        else if (time < 0.3f) {
            timeLabel.setDrawable(assets.clock_icon.get(2));
        }
        else if (time < 0.4f) {
            timeLabel.setDrawable(assets.clock_icon.get(3));
        }
        else if (time < 0.5f) {
            timeLabel.setDrawable(assets.clock_icon.get(4));
        }
        else if (time < 0.6f) {
            timeLabel.setDrawable(assets.clock_icon.get(5));
        }
        else if (time < 0.7f) {
            timeLabel.setDrawable(assets.clock_icon.get(6));
        }
        else if (time < 0.8f) {
            timeLabel.setDrawable(assets.clock_icon.get(7));
        }
        else {
            timeLabel.setDrawable(assets.clock_icon.get(8));
        }

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
        oldBandwidth = this.bandwidth;
        this.bandwidth = bandwidth;
        elapsedBandwidth = 0;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public int getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(int workplaces) {
        this.workplaces = workplaces;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        oldMoney = this.money;
        this.money = money;
        elapsedMoney = 0;
    }
}
