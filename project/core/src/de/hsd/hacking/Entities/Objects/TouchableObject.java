package de.hsd.hacking.Entities.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.hsd.hacking.Utils.Direction;
import de.hsd.hacking.Entities.Touchable;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.Shader;

public abstract class TouchableObject extends Object implements Touchable {

    private boolean touched;
    private Rectangle bounds;
    private ShapeRenderer debugRenderer;
    private ShaderProgram outlineShader;
    private int outlineFrames = 0;

    public TouchableObject(TextureRegion drawableRegion, boolean blocking, boolean interactable, Direction occupyDirection, int occupyAmount) {
        super(drawableRegion, blocking, true, interactable, occupyDirection, occupyAmount);
        this.touched = false;
        this.bounds = new Rectangle();
        this.outlineShader = new ShaderProgram(Shader.VERTEX_SHADER, Shader.OUTLINE_FRAGMENT_SHADER);
        if (!outlineShader.isCompiled()) {
            throw new GdxRuntimeException("Couldn't compile shader: " + outlineShader.getLog());
        }
        if(drawableRegion != null) {
            bounds.setSize(drawableRegion.getRegionWidth(), drawableRegion.getRegionHeight());
        }
        else {
            bounds.setSize(0, 0);
        }

        if (Constants.DEBUG) {
            this.debugRenderer = new ShapeRenderer();
        }
    }

    @Override
    public boolean touchDown(Vector2 position) {
        if (bounds.contains(position)) {
            touched = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 position) {
        boolean t = false;
        if (touched && bounds.contains(position)) {
            outlineFrames += 10;
            onTouch();
            t = true;
        }
        touched = false;
        return t;
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


        if (outlineFrames > 0) {
            batch.end();
            outlineShader.begin();
            outlineShader.setUniformf("u_viewportInverse", new Vector2(1f / drawableRegion.getTexture().getWidth(), 1f / drawableRegion.getTexture().getHeight()));
            outlineShader.end();
            batch.setShader(outlineShader);
            batch.begin();
        }
        super.draw(batch, parentAlpha);

        if (outlineFrames > 0) {
            outlineFrames--;
            batch.setShader(null);
        }

        if (Constants.DEBUG) {
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
