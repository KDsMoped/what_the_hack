package de.hsd.hacking.Entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Interface that has methods for objects that can be touched by the player.
 */
public interface Touchable {

    boolean touchDown(Vector2 position);

    boolean touchUp(Vector2 position);

    void touchDragged(Vector2 position);

}
