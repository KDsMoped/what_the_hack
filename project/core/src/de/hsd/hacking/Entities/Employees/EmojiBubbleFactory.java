package de.hsd.hacking.Entities.Employees;


import com.badlogic.gdx.utils.Timer;

import de.hsd.hacking.Entities.Entity;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by Florian Kaulfersch on 28.06.2017.
 */

public class EmojiBubbleFactory {

    private GameStage gameStage;
    private Timer timer;

    public static EmojiBubbleFactory instance = new EmojiBubbleFactory();

    public void initialize(GameStage stage){
        gameStage = stage;
    }

    private EmojiBubbleFactory(){
        this.timer = new Timer();
    }

    public enum EmojiType {
        SUCCESS, FAILURE, SPEAKING
    }

    public static void show(EmojiType type, Entity entity) {
        //TODO emojiBubble an employee Position zeigen, danach wieder entfernen
    }

}
