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
    private final int STATUS_BAR_HEIGHT = 18;
    private final int STATUS_BAR_ANIMATION_TIME = 2;

    private Assets assets;

    // StatusBar items
    private int date = 1;
    private float time = 0.0f;
    private int bandwidth = 0;
    private int employees = 0;
    private int workplaces = 0;
    private int money = 0;

    private int displayedMoney = money;
    private float elapsedMoney = 0;
    private int oldMoney = money;
    private int displayedBandwidth = bandwidth;
    private float elapsedBandwidth= 0;
    private int oldBandwidth = bandwidth;

    private Image moneyLabel;
    private Image bandwidthLabel;
    private Image employeesLabel;
    private Image timeLabel;

    private Label.LabelStyle titlebarStyle;
    private Label moneyText;
    private Label bandwidthText;
    private Label dateText;
    private Label employeesText;

    private ShapeRenderer backgroundRenderer;
    private Table items;

    private final DateFormat df = new SimpleDateFormat("dd MMM");

    public StatusBar(Assets assets) {
        this.assets = assets;
        backgroundRenderer = new ShapeRenderer();
        items = new Table();
        items.align(Align.bottomLeft);
        items.setPosition(0, GameStage.VIEWPORT_HEIGHT - STATUS_BAR_HEIGHT);
        items.setWidth(GameStage.VIEWPORT_WIDTH * (2.0f / 3.0f));

        titlebarStyle = new Label.LabelStyle();
        titlebarStyle.font = assets.status_bar_font;

        moneyLabel = new Image(assets.money_icon, Scaling.none, Align.top);
        bandwidthLabel = new Image(assets.bandwith_icon, Scaling.none, Align.bottom);
        employeesLabel = new Image(assets.employees_icon, Scaling.none, Align.bottom);
        timeLabel = new Image(assets.clock_icon.first(), Scaling.none, Align.bottom);

        moneyText = new Label("", titlebarStyle);
        bandwidthText = new Label("", titlebarStyle);
        dateText = new Label("", titlebarStyle);
        employeesText = new Label("", titlebarStyle);


        items.add(moneyLabel);
        items.add(moneyText).padRight(6).padTop(1);
        items.add(bandwidthLabel).padRight(2);
        items.add(bandwidthText).padRight(6).padTop(1);
        items.add(employeesLabel);
        items.add(employeesText).padRight(70).padTop(1);
        items.add(dateText).align(Align.right).padRight(15).padTop(1);
        items.add(timeLabel).align(Align.right);

        if (Constants.DEBUG) {
            Timer.schedule(new Timer.Task(){
                               @Override
                               public void run() {
                                   simulateBandwidth();
                                   simulateMoney();
                                   simulateTime();
                               }
                           }
                    , 2        //    (delay)
                    , 5     //    (seconds)
            );
        }
    }

    private void simulateBandwidth() {
        if (bandwidth < 1) {
            setBandwith(2000);
        }
        else {
            setBandwith(0);
        }
    }

    private void simulateMoney() {
        if (money < 1) {
            setMoney(300);
        }
        else {
            setMoney(0);
        }
    }

    private void simulateTime() {
        if (time < 1f) {
            setTime(time + 0.1f);
        }
        else {
            setTime(0f);
        }
    }

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

        moneyText.setText(String.format(Locale.GERMAN, "%05d", displayedMoney));
        bandwidthText.setText(String.format(Locale.GERMAN, "%04d", displayedBandwidth));
        employeesText.setText(Integer.toString(employees) + "/" + Integer.toString(workplaces));
        dateText.setText(df.format(ConvertDaysToDate(date)));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Draw status bar background
        batch.end();
        backgroundRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        backgroundRenderer.setTransformMatrix(batch.getTransformMatrix());
        backgroundRenderer.setColor(Color.GRAY);
        backgroundRenderer.begin(ShapeRenderer.ShapeType.Filled);
        backgroundRenderer.rect(0, GameStage.VIEWPORT_HEIGHT - STATUS_BAR_HEIGHT, GameStage.VIEWPORT_WIDTH * (2.0f / 3.0f), STATUS_BAR_HEIGHT);
        backgroundRenderer.end();
        batch.begin();

        items.draw(batch, parentAlpha);
    }

    private int AnimateIntChange(int newValue, int oldValue, float elapsed) {
        int animatedValue;

        float progress = Math.min(1.0f, elapsed / STATUS_BAR_ANIMATION_TIME);
        float interpol = Interpolation.sineOut.apply(progress);

        animatedValue = (int)((newValue - oldValue) * interpol);

        return animatedValue + oldValue;
    }

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
