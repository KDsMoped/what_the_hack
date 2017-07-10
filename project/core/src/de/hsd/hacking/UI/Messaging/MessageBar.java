package de.hsd.hacking.UI.Messaging;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * Created by ju on 06.07.17.
 */

/**
 * UI element to display messages for the player.
 */
public class MessageBar extends Table implements EventListener, EventSender{
    private final static EventListener.EventType TYPE = EventType.MESSAGE_FINISHED_DISPLAYING;
    private final int COMPACT_HEIGHT = 21;
    private final int FULL_HEIGHT = 200;
    private final int SCROLLING_TEXT_CHARS = 45;
    private final float SCROLLING_SPEED = 0.3f;
    private final int INITIAL_WAIT = 2;
    private final int FINAL_WAIT = 3;

    private Table compactView;
    private Label compactText;
    private Image compactType;
    private Image compactArrow;
    private Message currentMessage;

    private Table fullView;
    private ScrollPane fullScroller;
    private VerticalGroup fullContainer;

    private Boolean compact = true;

    // Variables for the scrolling in animation
    private Boolean scrollMessage = false;
    private int scrollPosition = 0;
    private float scrollDelta = 0f;
    private Boolean finishedMessage = true;

    public MessageBar() {
        initTable();
        initCompactTable();
        initFullTable();

        this.add(compactView);

        MessageManager.instance().addListener(this);

        MessageManager.instance().Info("Dies ist ein super langer test Text und alle haben Versagt und wurden geschnappt oder auch nicht?!?", null);
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
        compactView.align(Align.left);
        compactText = new Label("", Constants.TerminalLabelStyle());
        compactText.setWrap(false);
        compactText.setAlignment(Align.center);
        compactArrow = new Image(Assets.instance().ui_up_arrow_inverted);
        compactType = new Image();

        compactView.add(compactType).left();
        compactView.add(compactText).expand().fill().width(GameStage.VIEWPORT_WIDTH - 40).pad(3);
        compactView.add(compactArrow).right();
    }

    private void initFullTable() {
        fullView = new Table();
        fullView.align(Align.topLeft);
        fullContainer = new VerticalGroup();
        fullScroller = new ScrollPane(fullContainer);
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
        currentMessage = MessageManager.instance().getCurrent();

        if (fullContainer.getChildren().size > 99) {
            fullContainer.removeActor(fullContainer.getChildren().first());
        }

        fullContainer.addActor(new MessageUIElement(currentMessage));

        compactType.setDrawable(Message.GetTypeIcon(currentMessage));

        if (currentMessage.getText().length() > SCROLLING_TEXT_CHARS) {
            scrollMessage = true;
            scrollPosition = 0;
            finishedMessage = false;

            compactText.setText(currentMessage.getText().substring(0, SCROLLING_TEXT_CHARS));
        }
        else {
            scrollMessage = false;
            finishedMessage = true;
            compactText.setText(currentMessage.getText());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // this is the scrolling mechanism
        // we only want to scroll if we are in the compact view and when the message is longer than the desired value
        if (compact && scrollMessage) {
            // if the message is new we wait INITIAL_WAIT before scrolling
            if (scrollPosition == 0 && scrollDelta < INITIAL_WAIT) {
                // just wait
            }
            // the initial wait is finished
            else {
                // Here we control the scrolling speed
                if (scrollDelta > SCROLLING_SPEED) {
                    scrollDelta = 0f;

                    // Are we at the end of the message?
                    if (currentMessage.getText().length() > (scrollPosition + SCROLLING_TEXT_CHARS)) {
                        scrollPosition++;

                        // Set the actual displayed substring
                        compactText.setText(currentMessage.getText().substring(scrollPosition, SCROLLING_TEXT_CHARS + scrollPosition));
                    }
                    else {
                        scrollMessage = false;
                        finishedMessage = true;
                    }
                }
            }

            scrollDelta += delta;
        }

        if (finishedMessage) {
            if (scrollDelta > FINAL_WAIT) {

                notifyListeners(TYPE);
            }

            scrollDelta += delta;
        }
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
