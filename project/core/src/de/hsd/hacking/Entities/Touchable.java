package de.hsd.hacking.Entities;

import com.badlogic.gdx.math.Vector2;

public interface Touchable {

    boolean touchDown(Vector2 position);

    boolean touchUp(Vector2 position);

    void touchDragged(Vector2 position);

}
