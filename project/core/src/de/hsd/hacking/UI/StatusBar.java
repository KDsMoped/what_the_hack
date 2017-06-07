package de.hsd.hacking.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

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
    private final int STATUS_BAR_HEIGHT = 16;

    private Assets assets;

    // StatusBar items
    private int date = 1;
    private float time = 0.0f;
    private int bandwidth = 0;
    private int employees = 0;
    private int workplaces = 0;

    private int money = 0;
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

    private final DateFormat df = new SimpleDateFormat("dd MMMM");

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
        timeLabel = new Image(assets.clock_icon, Scaling.none, Align.bottom);

        moneyText = new Label("", titlebarStyle);
        bandwidthText = new Label("", titlebarStyle);
        dateText = new Label("", titlebarStyle);
        employeesText = new Label("", titlebarStyle);


        items.add(moneyLabel).padTop(1).padRight(2);
        items.add(moneyText).padRight(6);
        items.add(bandwidthLabel).padTop(1).padRight(2);
        items.add(bandwidthText).padRight(6);
        items.add(employeesLabel).padTop(1).padRight(2);
        items.add(employeesText).padRight(15);
        items.add(dateText).align(Align.right).padRight(15);
        items.add(timeLabel).padTop(1).align(Align.right);
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

        moneyText.setText(String.format(Locale.GERMAN, "%05d", money));
        bandwidthText.setText(String.format(Locale.GERMAN, "%04d", bandwidth));
        employeesText.setText(Integer.toString(employees) + "/" + Integer.toString(workplaces));
        dateText.setText(df.format(ConvertDaysToDate(date)));

        items.draw(batch, parentAlpha);
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
        this.bandwidth = bandwidth;
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
        this.money = money;
    }
}
