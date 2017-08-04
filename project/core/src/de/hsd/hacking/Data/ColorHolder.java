package de.hsd.hacking.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores color values of the texture that need to be changed and the values they can be changed to
 * @author Florian
 */

public class ColorHolder {

    public static String Trousers = "64.0/255.0";
    public static String Shoes = "45.0/255.0";
    public static String Shirt = "113.0/255.0";
    public static String Skin = "210.0/255.0";
    public static String Eyes = "124.0/255.0";
    public static String Hair = "150.0/255.0";
    public static List<String> TrouserColors = new ArrayList<String>(Arrays.asList("4649c3ff", "4687c3ff", "6e4208ff", "291004ff"));
    public static List<String> ShoesColors = new ArrayList<String>(Arrays.asList("440849ff", "3e1c09ff", "d30000ff", "3e3e3eff"));
    public static List<String> ShirtColors = new ArrayList<String>(Arrays.asList("839fdfff", "28caa2ff", "9eca28ff", "7428caff", "8a0a1eff", "550c8aff", "111111ff", "eeeeeeff", "550c8aff"));
    public static List<String> SkinColors = new ArrayList<String>(Arrays.asList("e9c38cff", "dfca83ff", "eec39aff", "3b1d04ff", "7a3c09ff", "e5d8ceff", "ad8d6eff"));
    public static List<String> EyesColors = new ArrayList<String>(Arrays.asList("165394ff", "165394ff", "145747ff", "145747ff", "2b0900ff", "2b0900ff", "244211ff", "244211ff", "ffffffff", "d40b0bff"));
    public static List<String> HairColors = new ArrayList<String>(Arrays.asList("d40b0bff", "e9df3eff", "e9df3eff", "bab463ff", "bab463ff",
            "e9a33eff", "543202ff", "543202ff", "ae4512ff", "ae4512ff", "000000ff", "000000ff", "07acbaff", "ee6ee1ff"));
}
