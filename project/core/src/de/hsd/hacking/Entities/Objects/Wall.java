package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 06.06.2017.
 */

public class Wall extends Object {

    public Wall() {
        super(null, true, false, Direction.DOWN, 0);
    }
}
