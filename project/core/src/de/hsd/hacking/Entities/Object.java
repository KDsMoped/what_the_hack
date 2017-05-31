package de.hsd.hacking.Entities;

import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public class Object extends Entity {

    public Object(GameStage stage, boolean blocking) {
        super(stage, blocking);
    }

    @Override
    public String getName() {
        return "";
    }
}
