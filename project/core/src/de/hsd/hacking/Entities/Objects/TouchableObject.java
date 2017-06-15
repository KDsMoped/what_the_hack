package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by Cuddl3s on 13.06.2017.
 */

public abstract class TouchableObject extends Object implements Touchable {

    private boolean touched;
    private Rectangle bounds;
    private ShapeRenderer debugRenderer;

    public TouchableObject(TextureRegion drawableRegion, boolean blocking, boolean repositionable, Direction occupyDirection, int occupyAmount) {
        super(drawableRegion, blocking, repositionable, occupyDirection, occupyAmount);
        this.touched = false;
        this.bounds = new Rectangle();
        bounds.setSize(drawableRegion.getRegionWidth(), drawableRegion.getRegionHeight());
        if (Constants.DEBUG){
            this.debugRenderer = new ShapeRenderer();
        }
    }

    @Override
    public void touchDown(Vector2 position) {
        if (bounds.contains(position)){
            touched = true;
        }
    }

    @Override
    public void touchUp(Vector2 position) {
        if (touched && bounds.contains(position)){
            onTouch();
        }
        touched = false;
    }

    @Override
    public void touchDragged(Vector2 position) {
        //stub
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        bounds.setPosition(position);
    }

    public abstract void onTouch();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (Constants.DEBUG){
            batch.end();
            debugRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            debugRenderer.setTransformMatrix(batch.getTransformMatrix());
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(touched ? Color.GREEN : Color.RED);
            debugRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            debugRenderer.end();
            batch.begin();
        }
    }
}