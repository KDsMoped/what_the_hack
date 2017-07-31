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
import de.hsd.hacking.Proto;
import de.hsd.hacking.Utils.Callback.Callback;
import de.hsd.hacking.Utils.DateUtils;


/**
 * This class represents a message to the user.
 */
public class Message{
    Proto.Message.Builder data;

    // TODO EventListener serialization
    private ClickListener listener;

    public Message() {
        this.data = Proto.Message.newBuilder();
    }

    public Message(Proto.Message.Builder data) {
        this.data = data;
    }

    public Message(String text, Date date, Proto.Message.Type type, ClickListener listener) {
        this.data = Proto.Message.newBuilder();

        data.setMessage(text);
        data.setDate(DateUtils.ConvertDateToDays(date));
        this.listener = listener;
        data.setType(type);
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
        return data.getMessage();
    }

    public void setText(String text) {
        data.setMessage(text);
    }

    public Date getDate() {
        return DateUtils.ConvertDaysToDate(data.getDate());
    }

    public void setDate(Date date) {
        data.setDate(DateUtils.ConvertDateToDays(date));
    }

    public ClickListener getListener() {
        return listener;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public Proto.Message.Type getType() {
        return data.getType();
    }

    public void setType(Proto.Message.Type type) {
        data.setType(type);
    }

    public Proto.Message.Builder getData() {
        return data;
    }
}
