package de.hsd.hacking.UI.Messaging;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Utils.Constants;

public class MessageUIElement extends Table {
    private Message message;

    public MessageUIElement() {

    }

    public MessageUIElement(Message message) {
        this.message = message;

        initUI();
    }

    private void initUI() {
        this.clearChildren();
        this.setTouchable(Touchable.enabled);

        if (message.getListener() != null) {
            this.addListener(message.getListener());
        }

        Image img = new Image(Message.GetTypeIcon(message));

        Label date = new Label(Constants.dateFormat.format(message.getDate()), Constants.TerminalLabelStyle());

        Label text = new Label(message.getText(), Constants.TerminalLabelStyle());
        text.setWrap(true);

        this.add(img).pad(4).top();
        this.add(date).pad(4).top();
        this.add(text).width(380).pad(4);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
        initUI();
    }
}
