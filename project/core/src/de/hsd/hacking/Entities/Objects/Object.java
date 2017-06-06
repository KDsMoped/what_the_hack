package de.hsd.hacking.Entities.Objects;

import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Cuddl3s on 30.05.2017.
 */

public abstract class Object extends Entity {

    private boolean repositionable;

    public Object(boolean blocking, boolean repositionable) {
        super(blocking);
        this.repositionable = repositionable;
    }

    @Override
    public String getName() {
        return "";
    }

    public boolean isRepositionable(){
        return repositionable;
    }
}
