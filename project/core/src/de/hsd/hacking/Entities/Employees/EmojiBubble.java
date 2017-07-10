package de.hsd.hacking.Entities.Employees;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;

import de.hsd.hacking.Entities.Entity;

/**
 * Created by Cuddl3s on 09.07.2017.
 */

public class EmojiBubble extends Actor {

    private TextureRegion emoji;
    private Vector2 position;

    /**
     * Convenience method with default values
     * @param entity entity which receives the bubble
     * @param emoji1 desired emoji graphic
     */
    public EmojiBubble(final Entity entity, final TextureRegion emoji1) {
        this(entity, emoji1, 0.3f, 0.8f, 0.3f);
    }

    /**
     * Creates an emoji bubble graphic over given entity. Gets destroyed after animation is done.
     * @param entity entity which receives the bubble
     * @param emoji1 desired emoji graphic
     * @param in seconds to fade in
     * @param stay seconds staying with alpha = 1
     * @param out seconds to fade out
     */
    public EmojiBubble(final Entity entity, final TextureRegion emoji1, float in, float stay, float out) {
        this.position = entity.getPositionReference();
        this.emoji = emoji1;
        addAction(Actions.sequence(Actions.fadeOut(0f), Actions.fadeIn(in), Actions.delay(stay), Actions.fadeOut(out), Actions.run(new Runnable() {
            @Override
            public void run() {
                remove();
            }
        })));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //setColor needed for fadein/ fadeout
        batch.setColor(getColor());
        batch.draw(emoji, position.x, position.y + 48f);
        batch.setColor(Color.WHITE);
    }

    @Override
    /**
     * Act method overide. Needed for fadein /fadeout
     */
    public void act(float delta) {
        super.act(delta);
    }
}
