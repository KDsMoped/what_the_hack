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

    public EmojiBubble(final Entity entity, final TextureRegion emoji1) {
        this(entity, emoji1, 0.3f, 0.5f, 0.3f);
    }

    public EmojiBubble(final Entity entity, final TextureRegion emoji1, float in, float stay, float out) {
        this.position = entity.getPosition().add(0, 48f);
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
        batch.setColor(getColor());
        batch.draw(emoji, position.x, position.y);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
