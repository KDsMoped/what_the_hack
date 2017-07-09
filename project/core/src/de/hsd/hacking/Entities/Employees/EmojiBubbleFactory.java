package de.hsd.hacking.Entities.Employees;


import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Florian Kaulfersch on 28.06.2017.
 */

public class EmojiBubbleFactory {

    public static EmojiBubbleFactory instance = new EmojiBubbleFactory();

    public enum EmojiType {
        SUCCESS, FAILURE, SPEAKING
    }

    public static void show(EmojiType type, Entity entity) {
        switch (type) {

            case SUCCESS:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().success));
                break;
            case FAILURE:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().angry));
                break;
            case SPEAKING:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().success));
                break;
        }
    }

}
