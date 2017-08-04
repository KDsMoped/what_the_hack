package de.hsd.hacking.Utils;

import com.badlogic.gdx.graphics.Color;

import de.hsd.hacking.Data.ColorHolder;

/**
 * Shader for changing visual appearance.
 * @author Florian
 */
public class Shader {

    public static final String VERTEX_SHADER = "attribute vec4 a_position;    \n" +
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

    private static final String EMPLOYEE_COLOR_FRAGMENT_SHADER = "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform int sel;\n" +
            "uniform vec2 u_viewportInverse; \n" +
            "void main()                                  \n" +
            "{                                            \n" +
            "vec4 color = texture2D(u_texture, v_texCoords).rgba; \n" +
            "vec4 newColor = color; \n" +
            "if (color.a > 0.0){ \n" +
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
            "}else{ \n" +
            "if (sel == 1) { \n" +
            "if (v_texCoords.x < 1.0 && (texture2D(u_texture, vec2(v_texCoords.x + u_viewportInverse.x, v_texCoords.y) ).a > 0.0) || " +
            "(v_texCoords.x > 0.0 && texture2D(u_texture, vec2(v_texCoords.x - u_viewportInverse.x, v_texCoords.y)).a > 0.0) ||" +
            "(v_texCoords.y < 1.0 && texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + u_viewportInverse.y)).a > 0.0) || " +
            "(v_texCoords.y > 0.0 && texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - u_viewportInverse.y)).a > 0.0)) {" +
            "newColor = vec4(1.0, 1.0, 1.0, 1.0); \n" +
            "} \n" +
            "} \n" +
            "} \n" +
            "  gl_FragColor = newColor * v_color;\n" +
            "}";

    public static final String OUTLINE_FRAGMENT_SHADER = "#ifdef GL_ES\n" +
        "precision mediump float;\n" +
        "#endif\n" +
        "varying vec4 v_color;\n" +
        "varying vec2 v_texCoords;\n" +
        "uniform sampler2D u_texture;\n" +
        "uniform vec2 u_viewportInverse; \n" +
        "void main()                                  \n" +
        "{                                            \n" +
            "vec4 color = texture2D(u_texture, v_texCoords); \n" +
            "if (color.a < 1.0){ \n" +
                "if (v_texCoords.x < 1.0 && (texture2D(u_texture, vec2(v_texCoords.x + u_viewportInverse.x, v_texCoords.y) ).a > 0.0) || " +
                    "(v_texCoords.x > 0.0 && texture2D(u_texture, vec2(v_texCoords.x - u_viewportInverse.x, v_texCoords.y)).a > 0.0) ||" +
                    "(v_texCoords.y < 1.0 && texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + u_viewportInverse.y)).a > 0.0) || " +
                    "(v_texCoords.y > 0.0 && texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - u_viewportInverse.y)).a > 0.0)) {" +
                        "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0); \n" +
                "} else {" +
                    "gl_FragColor = color;" +
                "}" +
            "} else { \n" +
            "gl_FragColor = color;" +
            "}" +
        "}";

    /**
     * Creates a custom colorShader and returns it.
     * @param hairColor
     * @param skinColor
     * @param shirtColor
     * @param trousersColor
     * @param eyeColor
     * @param shoeColor
     * @return Shader with given colorValues.
     */
    public static String getEmployeeFragmentShader(Color hairColor, Color skinColor, Color shirtColor, Color trousersColor,
                                                   Color eyeColor, Color shoeColor){
        String trousers = trousersColor.r + "," +trousersColor.g + "," + trousersColor.b;
        String hair = hairColor.r + "," + hairColor.g + "," + hairColor.b;
        String skin = skinColor.r + "," + skinColor.g + "," + skinColor.b;
        String shirt = shirtColor.r + "," + shirtColor.g + "," + shirtColor.b;
        String eye = eyeColor.r + "," + eyeColor.g + "," + eyeColor.b;
        String shoe = shoeColor.r + "," + shoeColor.g + "," + shoeColor.b;

        String fragment = EMPLOYEE_COLOR_FRAGMENT_SHADER.replace("{{hairold}}", ColorHolder.Hair);
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
