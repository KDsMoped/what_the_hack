package de.hsd.hacking.Data.Messaging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.protobuf.GeneratedMessageV3;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.GameTime;
import de.hsd.hacking.Data.Manager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.DateUtils;

/**
 * This class manages all messages for the player.
 * @author Julian
 */
public class MessageManager implements Manager, EventSender{
    private final static EventListener.EventType TYPE = EventListener.EventType.MESSAGE_NEW;

    private static MessageManager instance;

    private Message message;

    private List<EventListener> listeners;

    private MessageManager() {
        instance = this;
        listeners = new ArrayList<EventListener>();
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

        Date date = DateUtils.ConvertDaysToDate(GameTime.instance().getCurrentDay());
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
    public static void createInstance() {
        if (instance != null){
            Gdx.app.error("", "ERROR: Instance of MessageManager has not been destroyed before creating new one.");
            return;
        }

        instance = new MessageManager();
    }

    public static MessageManager instance() {

        if (instance == null)
            Gdx.app.error("", "ERROR: Instance of MessageManager has not been created yet. Use createInstance() to do so.");

        return instance;
    }

    /**
     * Get the newest message.
     * @return current message
     */
    public Message getCurrent() {
        return message;
    }

    /**
     * Initializes this manager class in terms of references towards other objects. This is guaranteed to be called
     * after all other managers have been initialized.
     */
    @Override
    public void initReferences() {

    }

    /**
     * Creates the default state of this manager when a new game is started.
     */
    @Override
    public void loadDefaultState() {

    }

    /**
     * Recreates the state this manager had before serialization.
     */
    @Override
    public void loadState() {

    }

    /**
     * Destroys manager this instance.
     */
    @Override
    public void cleanUp() {
        instance = null;
    }
}
