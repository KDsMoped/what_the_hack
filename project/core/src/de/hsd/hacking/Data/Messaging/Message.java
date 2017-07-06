package de.hsd.hacking.Data.Messaging;

/**
 * Created by ju on 06.07.17.
 */

import com.google.gson.annotations.Expose;

import java.util.Date;

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
    private Callback listener;
    @Expose private Type type = Type.INFO;

    public Message() {

    }

    public Message(String text, Date date, Type type, Callback listener) {
        this.text = text;
        this.date = date;
        this.listener = listener;
        this.type = type;
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

    public Callback getListener() {
        return listener;
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
