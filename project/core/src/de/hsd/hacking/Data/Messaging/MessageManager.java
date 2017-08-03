package de.hsd.hacking.Data.Messaging;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Date;

import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.DateUtils;

/**
 * This class manages all messages for the player.
 * @author Julian Geywitz
 */
public class MessageManager implements EventSender{
    private final static EventListener.EventType TYPE = EventListener.EventType.MESSAGE_NEW;

    private static MessageManager instance;

    private Message message;

    public MessageManager() {
        instance = this;
    }

    /**
     * Send an info message to the user.
     * @param text
     */
    public void Info(String text){
        Info(text, null);
    }

    /**
     * Send an info message to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Info(String text, ClickListener listener) {
        Message message = CreateNewMessage(text, listener);
        message.setType(Proto.Message.Type.INFO);
        this.message = message;

        notifyListeners(TYPE);
    }

    /**
     * Send a warning to the user.
     * @param text
     */
    public void Warning(String text){
        Warning(text, null);
    }

    /**
     * Send a warning to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Warning(String text, ClickListener listener) {
        Message message = CreateNewMessage(text, listener);
        message.setType(Proto.Message.Type.WARNING);
        this.message = message;

        notifyListeners(TYPE);
    }

    /**
     * Send an error message to the user.
     * @param text Message text.
     */
    public void Error(String text) {
        Error(text, null);
    }

    /**
     * Send an error message to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Error(String text, ClickListener listener) {
        Message message = CreateNewMessage(text, listener);
        message.setType(Proto.Message.Type.ERROR);
        this.message = message;

        notifyListeners(TYPE);
    }

    /**
     * Send help to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Help(String text, ClickListener listener) {
        Message message = CreateNewMessage(text, listener);
        message.setType(Proto.Message.Type.HELP);
        this.message = message;

        notifyListeners(TYPE);
    }

    private Message CreateNewMessage(String text, ClickListener listener) {
        Message message = new Message();

        Date date = DateUtils.ConvertDaysToDate(GameTime.instance.getCurrentDay());
        message.setDate(date);
        message.setText(text);
        message.setListener(listener);

        return message;
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
            listener.OnEvent(type, message);
        }
    }

    /**
     * Global instance for the MessageManager.
     * @return Global instance.
     */
    public static MessageManager instance() {

        if (instance == null) return new MessageManager();
        return instance;
    }

    /**
     * Get the newest message.
     * @return current message
     */
    public Message getCurrent() {
        return message;
    }
}
