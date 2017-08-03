package de.hsd.hacking.Utils;

import com.badlogic.gdx.graphics.Color;

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
