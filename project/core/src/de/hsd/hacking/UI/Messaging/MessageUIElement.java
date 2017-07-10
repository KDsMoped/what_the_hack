package de.hsd.hacking.UI.Messaging;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Utils.Constants;
import de.hsd.hacking.Utils.DateUtils;

/**
 * Created by ju on 09.07.17.
 */

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

        Image img = new Image(Message.GetTypeIcon(message));

        Label date = new Label(Constants.dateFormat.format(message.getDate()), Constants.TerminalLabelStyle());

        Label text = new Label(message.getText(), Constants.TerminalLabelStyle());
        text.setWrap(true);

        this.add(img).pad(4);
        this.add(date).pad(4);
        this.add(text).width(400).pad(4);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
        initUI();
    }
}
