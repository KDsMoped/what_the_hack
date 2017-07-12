package de.hsd.hacking.Data.Messaging;

/**
 * Created by ju on 06.07.17.
 */

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.gson.annotations.Expose;

import java.util.Date;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Utils.Callback.Callback;


/**
 * This class represents a message to the user.
 */
public class Message{
    public enum Type { INFO, WARNING, ERROR, HELP };

    @Expose private String text = "";
    @Expose private Date date;
    // TODO EventListener serialization
    private ClickListener listener;
    @Expose private Type type = Type.INFO;

    public Message() {

    }

    public Message(String text, Date date, Type type, ClickListener listener) {
        this.text = text;
        this.date = date;
        this.listener = listener;
        this.type = type;
    }

    public static TextureRegionDrawable GetTypeIcon(Message message) {
        TextureRegionDrawable drawable;

        switch (message.getType()) {
            case INFO:
                drawable = Assets.instance().ui_info;
                break;
            case WARNING:
                drawable = Assets.instance().ui_warning;
                break;
            case HELP:
                drawable = Assets.instance().ui_help;
                break;
            case ERROR:
                drawable = Assets.instance().ui_error;
                break;
            default:
                drawable = Assets.instance().ui_info;
        }

        return drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ClickListener getListener() {
        return listener;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
