package de.hsd.hacking.UI.General;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Stages.GameStage;

/**
 * Created by ju on 06.07.17.
 */

/**
 * UI element to display messages for the player.
 */
public class MessageBar extends Table implements EventListener, EventSender{
    private final static EventListener.EventType TYPE = EventType.MESSAGE_FINISHED_DISPLAYING;
    private final int COMPACT_HEIGHT = 20;
    private final int FULL_HEIGHT = 200;

    private List<Message> messages;

    private Table compactView;
    private Table fullView;

    private Boolean compact = true;

    public MessageBar() {
        messages = new ArrayList<Message>();

        initTable();
        initCompactTable();
        initFullTable();
    }

    private void initTable() {
        this.setPosition(0, 0);
        this.setHeight(COMPACT_HEIGHT);
        this.setWidth(GameStage.VIEWPORT_WIDTH);
        this.setTouchable(Touchable.enabled);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ToggleView();
            }
        });
        this.setBackground(Assets.instance().terminal_patch);
    }

    private void initCompactTable() {
        compactView = new Table();
    }

    private void initFullTable() {
        fullView = new Table();
    }

    public void ToggleView() {
        if (compact) {
            // We are now in Full mode!
            compact = false;

            this.clearChildren();
            this.setHeight(FULL_HEIGHT);
            this.add(fullView);
        }
        else {
            // We are now in compact mode!
            compact = true;

            this.clearChildren();
            this.setHeight(COMPACT_HEIGHT);
            this.add(compactView);
        }
    }

    private void NewMessage() {
        Message message = MessageManager.instance().getCurrent();

    }

    @Override
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(EventListener.EventType type) {
        for (EventListener listener:listeners) {
            listener.OnEvent(type, this);
        }
    }

    @Override
    public void OnEvent(EventType type, Object sender) {
        if (type == EventType.MESSAGE_NEW) {
            NewMessage();
        }
    }
}
