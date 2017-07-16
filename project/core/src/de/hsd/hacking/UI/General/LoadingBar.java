package de.hsd.hacking.UI.General;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.hsd.hacking.Assets.Assets;

/**
 * Created by Florian Kaulfersch on 12.07.2017.
 */

public class LoadingBar extends Actor {

    private int segments;
    private TextureRegion bar;
    private TextureRegion segment;

    public LoadingBar(){
        bar = Assets.instance().loading_bar.get(0).getRegion();
        segment = Assets.instance().loading_bar.get(1).getRegion();
    }

    public void set(float current, float max){
        segments = MathUtils.clamp(MathUtils.floor((current / max) * 10), 0, 10);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(bar, getX() - bar.getRegionWidth() / 2f, getY() - bar.getRegionHeight() / 2f, bar.getRegionWidth(), bar.getRegionHeight());
        if (segments > 0){
            for (int i = 0; i < segments; i++) {
                batch.draw(segment, getX() - segment.getRegionWidth() / 2f + 3f * i, getY() - bar.getRegionHeight() / 2f, segment.getRegionWidth(), segment.getRegionHeight());
            }
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
