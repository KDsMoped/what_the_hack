package de.hsd.hacking.Utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Cuddl3s on 29.05.2017.
 */

public class ColorPair {

    public Color oldColor;
    public Color newColor;

    public ColorPair(Color oldColor, Color newColor){
        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    public ColorPair(String oldColorHex, String newColorHex){
        this.oldColor = Color.valueOf(oldColorHex);
        this.newColor = Color.valueOf(newColorHex);
    }

}
