package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Utils.Direction;

public class ObjectFactory {

    public static Object generateObject(ObjectType type, Assets assets){
        switch (type){
            case WALL:
                return new Object(null, true, false, false, Direction.DOWN, 0) {
                    @Override
                    public String getName() {
                        return "Wall";
                    }
                };
            case LAMP:
                return new Object(assets.lamp, true, false, false, Direction.DOWN, 0) {
                    @Override
                    public String getName() {
                        return "Lamp";
                    }
                };
            default:
                return null;
        }
    }
}
