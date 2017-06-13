package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Entities.Direction;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public abstract class Object extends Entity {

    private boolean repositionable;
    private Direction occupyDirection;
    private int occupyAmount;

    public Object(boolean blocking, boolean repositionable, Direction occupyDirection, int occupyAmount) {
        super(blocking);
        this.repositionable = repositionable;
        this.occupyDirection = occupyDirection;
        this.occupyAmount = occupyAmount;
    }

    @Override
    public String getName() {
        return "";
    }

    public boolean isRepositionable(){
        return repositionable;
    }
}
