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
        SUCCESS, FAILURE, OK, NO, SPEAKING, LEVELUP
    }

    /**
     * Creates an {@link EmojiBubble} with given type over given entity
     * @param type Changes the texture uses (SUCCESS, FAILURE, SPEAKING)
     * @param entity The entity at which's position the bubble will be created
     */
    public static void show(final EmojiType type, final Entity entity) {
        switch (type) {

            case SUCCESS:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_success));
                break;
            case FAILURE:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_angry));
                break;
            case OK:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_ok));
                break;
            case NO:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_no));
                break;
            case SPEAKING:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_success));
                break;
            case LEVELUP:
                GameStage.instance().addToUILayer(new EmojiBubble(entity, Assets.instance().emoji_levelup));
                break;
        }
    }

}
