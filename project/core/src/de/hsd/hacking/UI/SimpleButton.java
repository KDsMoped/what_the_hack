package de.hsd.hacking.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;


/**
 * Created by Cuddl3s on 21.04.2017.
 */

public class SimpleButton extends Button {

    private String text;
    private Runnable task;
    private BitmapFont font;
    private float textHeight;

    public SimpleButton(Vector2 bottomCenterPosition, String text, Assets assets, Runnable task){
        super(bottomCenterPosition, assets.testButton);
        this.text = text;
        this.task = task;
        this.font = assets.standard_font;
        GlyphLayout layout = new GlyphLayout();
        layout.setText(this.font, text);
        this.textHeight = layout.height;
    }


    @Override
    public boolean touchDown(Vector2 coords) {
        return super.touchDown(coords);
    }

    @Override
    public boolean touchUp(Vector2 coords) {
        if(super.touchUp(coords)){
            task.run();
            return true;
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float alpha){
        super.draw(batch, alpha);
        font.draw(batch, text, bounds.x,
                pressed ? bounds.y + bounds.height * .6f + textHeight * .5f - 5f :
                        bounds.y + bounds.height * .6f + textHeight * .5f,
                bounds.width, Align.center, false);
    }
}
