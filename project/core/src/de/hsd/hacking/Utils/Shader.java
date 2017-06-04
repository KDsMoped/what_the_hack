package de.hsd.hacking.Utils;

import com.badlogic.gdx.graphics.Color;

import de.hsd.hacking.Data.ColorHolder;

/**
 * Created by Cuddl3s on 04.06.2017.
 */

public class Shader {

    public static final String vertexShader = "attribute vec4 a_position;    \n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 v_color;" +
            "varying vec2 v_texCoords;" +
            "void main()                  \n" +
            "{                            \n" +
            "v_color = a_color; \n" +
            "   v_texCoords = a_texCoord0; \n" +
            "   gl_Position =  u_projTrans * a_position;  \n"      +
            "}                            \n" ;

    private static final String fragmentShader = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "void main()                                  \n" +
            "{                                            \n" +
            "vec4 color = texture2D(u_texture, v_texCoords).rgba; \n" +
            "vec4 newColor; \n" +
            "if (color.r == {{trousersold}} && color.g == {{trousersold}} && color.b == {{trousersold}}){ //trousers \n" +
            "newColor = vec4({{trousers}}, 1.0); \n" +
            "}else if (color.r == {{shirtold}} && color.g == {{shirtold}} && color.b == {{shirtold}}){ \n" +
            "newColor = vec4({{shirt}}, 1.0); \n" +
            "} \n" +
            "else if (color.r == {{skinold}} && color.g == {{skinold}} && color.b == {{skinold}}){ \n" +
            "newColor = vec4({{skin}}, 1.0); \n" +
            "} \n" +
            "else if (color.r == {{hairold}} && color.g == {{hairold}} && color.b == {{hairold}}){ \n" +
            "newColor = vec4({{hair}}, 1.0); \n" +
            "} \n" +
            "else if (color.r == {{shoeold}} && color.g == {{shoeold}} && color.b == {{shoeold}}){ \n" +
            "newColor = vec4({{shoe}}, 1.0); \n" +
            "} \n" +
            "else if (color.r == {{eyeold}} && color.g == {{eyeold}} && color.b == {{eyeold}}){ \n" +
            "newColor = vec4({{eye}}, 1.0); \n" +
            "} \n" +
            "else{ \n" +
            "newColor = color; \n" +
            "} \n" +
            "  gl_FragColor = newColor * v_color;\n" +
            "}";

    public static String getFragmentShader(Color hairColor, Color skinColor, Color shirtColor, Color trousersColor,
                                    Color eyeColor, Color shoeColor){
        String trousers = trousersColor.r + "," +trousersColor.g + "," + trousersColor.b;
        String hair = hairColor.r + "," + hairColor.g + "," + hairColor.b;
        String skin = skinColor.r + "," + skinColor.g + "," + skinColor.b;
        String shirt = shirtColor.r + "," + shirtColor.g + "," + shirtColor.b;
        String eye = eyeColor.r + "," + eyeColor.g + "," + eyeColor.b;
        String shoe = shoeColor.r + "," + shoeColor.g + "," + shoeColor.b;

        String fragment = fragmentShader.replace("{{hairold}}", ColorHolder.Hair);
        fragment = fragment.replace("{{trousersold}}", ColorHolder.Trousers);
        fragment = fragment.replace("{{skinold}}", ColorHolder.Skin);
        fragment = fragment.replace("{{shirtold}}", ColorHolder.Shirt);
        fragment = fragment.replace("{{eyeold}}", ColorHolder.Eyes);
        fragment = fragment.replace("{{shoeold}}", ColorHolder.Shoes);

        fragment = fragment.replace("{{hair}}", hair);
        fragment = fragment.replace("{{trousers}}", trousers);
        fragment = fragment.replace("{{skin}}", skin);
        fragment = fragment.replace("{{shirt}}", shirt);
        fragment = fragment.replace("{{eye}}", eye);
        fragment = fragment.replace("{{shoe}}", shoe);

        return fragment;
    }


}