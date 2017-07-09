package de.hsd.hacking.Data.Messaging;


/**
 * Created by ju on 06.07.17.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.DateUtils;

/**
 * This class manages all messages for the player.
 */
public class MessageManager implements EventSender, EventListener{
    private final static EventListener.EventType TYPE = EventListener.EventType.MESSAGE_NEW;

    private static MessageManager instance;

    private List<Message> messages;

    private int currentMessage = -1;

    private Boolean newMessage = false;
    private Boolean finishedDisplaying = true;

    public MessageManager() {
        messages = new ArrayList<Message>(100);
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
    public void Info(String text, Callback listener) {
        Message message = CreateNewMessage(text, listener);

        message.setType(Message.Type.INFO);

        messages.add(message);
        Process();
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
    public void Warning(String text, Callback listener) {
        Message message = CreateNewMessage(text, listener);

        message.setType(Message.Type.WARNING);

        messages.add(message);
        Process();
    }

    /**
     * Send an error message to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Error(String text, Callback listener) {
        Message message = CreateNewMessage(text, listener);

        message.setType(Message.Type.ERROR);

        messages.add(message);
        Process();
    }

    /**
     * Send help to the user.
     * @param text Message text.
     * @param listener Callback.
     */
    public void Help(String text, Callback listener) {
        Message message = CreateNewMessage(text, listener);

        message.setType(Message.Type.HELP);

        messages.add(message);
        Process();
    }

    private Message CreateNewMessage(String text, Callback listener) {
        Message message = new Message();

        newMessage = true;

        Date date = DateUtils.ConvertDaysToDate(GameTime.instance.getCurrentDay());
        message.setDate(date);
        message.setText(text);
        message.setListener(listener);

        return message;
    }

    private void Process() {
        if (newMessage && finishedDisplaying) {
            newMessage = false;
            finishedDisplaying = false;
        }
        else {
            return;
        }

        if (currentMessage < 98) {
            currentMessage++;
        }
        else {
            messages.remove(0);
        }

        notifyListeners(TYPE);
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

    /**
     * Global instance for the MessageManager.
     * @return Glocal instance.
     */
    public static MessageManager instance() {

        if (instance == null) instance = new MessageManager();
        return instance;
    }

    /**
     * Get a readonly version of the queue.
     * @return readonly queue.
     */
    public List<Message> getQueue() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Get the current message object.
     * @return Current message.
     */
    public Message getCurrent() {
        return messages.get(currentMessage);
    }

    @Override
    public void OnEvent(EventType type, Object sender) {
        if (type == EventType.MESSAGE_FINISHED_DISPLAYING) {
            finishedDisplaying = true;
            Process();
        }
    }
}
