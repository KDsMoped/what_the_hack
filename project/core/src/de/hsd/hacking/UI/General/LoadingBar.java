package de.hsd.hacking.UI.General;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Florian Kaulfersch on 12.07.2017.
 */

public class LoadingBar extends Actor {

    private static final int SEGMENT_AMOUNT = 10;
    private static final float LIGHT_UP_TIME = 0.2f;
    private static final float VALUE_PER_STEP_SLOW = 2/9f;
    private static final float VALUE_PER_STEP_MEDIUM = 4/9f;
    private static final int BLINK_EVERY = 2;

    private int segments;
    private TextureRegion bar;
    private TextureRegion segment;
    private TextureRegion segment_active;
    private TextureRegion segment_full;
    private float current;
    private boolean active;
    private float elapsedTime;
    private float speedAdjust;
    private boolean full;

    public LoadingBar(){
        bar = Assets.instance().loading_bar.get(0).getRegion();
        segment = Assets.instance().loading_bar.get(1).getRegion();
        segment_active = Assets.instance().loading_bar.get(2).getRegion();
        segment_full = Assets.instance().loading_bar.get(3).getRegion();
        setHeight(bar.getRegionHeight());
        setWidth(bar.getRegionWidth());
        elapsedTime = 0f;
        speedAdjust = 2f;
    }

    public void set(float current, float max){
        if (current >= max) {
            Gdx.app.log(Constants.TAG, "Full = true");
            this.full = true;
        } else {
            if (current == this.current) {
                active = false;
            } else {
                float change = (current - this.current);
                if (change <= VALUE_PER_STEP_SLOW) {
                    speedAdjust = 2f;
                    Gdx.app.log(Constants.TAG, "SLOW CHANGE");
                } else if (change <= VALUE_PER_STEP_MEDIUM) {
                    speedAdjust = 1f;
                    Gdx.app.log(Constants.TAG, "MEDIUM CHANGE");
                } else {
                    speedAdjust = 0.5f;
                    Gdx.app.log(Constants.TAG, "FAST CHANGE");
                }
                active = true;
            }
        }
        this.current = current;
        segments = MathUtils.clamp(MathUtils.floor((current / max) * SEGMENT_AMOUNT), 0, SEGMENT_AMOUNT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(bar, getX(), getY(), bar.getRegionWidth(), bar.getRegionHeight());
        if (segments > 0) {
            for (int i = 0; i < segments; i++) {
                if (!full){
                    if (!active) {
                        batch.draw(segment, getX() + 3f * i, getY(), segment.getRegionWidth(), segment.getRegionHeight());
                    } else {
                        batch.draw(blink(i) ? segment_active : segment, getX() + 3f * i, getY(), segment.getRegionWidth(), segment.getRegionHeight());
                    }
                } else {
                    batch.draw(segment_full, getX() + 3f * i, getY(), segment_full.getRegionWidth(), segment_full.getRegionHeight());
                }
            }
        }
    }

    private boolean blink(int i) {
        return (elapsedTime % (BLINK_EVERY * speedAdjust) >= i * LIGHT_UP_TIME * speedAdjust) &&
                (elapsedTime % (BLINK_EVERY * speedAdjust) < (i + 1) * LIGHT_UP_TIME * speedAdjust);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
    }
}
