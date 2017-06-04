package de.hsd.hacking.Entities.Equipment;

import de.hsd.hacking.Entities.Object;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by domin on 31.05.2017.
 */

public abstract class Equipment extends Object {
    public Equipment(GameStage stage, boolean blocking) {
        super(stage, blocking);
    }
}
