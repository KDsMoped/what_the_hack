package de.hsd.hacking.UI.Messaging;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Assets.AudioManager;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Data.ProtobufHandler;
import de.hsd.hacking.Data.SaveGameManager;
import de.hsd.hacking.Proto;
import de.hsd.hacking.Screens.ScreenManager;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * UI element to display messages for the player.
 * @author Julian
 */
// TODO scroll down
public class MessageBar extends Table implements EventListener, ProtobufHandler {
    private final int COMPACT_HEIGHT = 17;
    private final int SCROLLING_TEXT_CHARS = 59;
    private final int INITIAL_WAIT = 1;
    private final int MESSAGE_BUFFER = 100;

    private Table compactView;
    private Label compactText;
    private Image compactType;

    private List<Message> messages;
    private int compactPosition = -1;

    private Table fullView;
    private ScrollPane fullScroller;
    private VerticalGroup fullContainer;

    private Boolean compact = true;

    private Boolean visible = false;
    private Boolean isHiding = false;
    private Boolean isShowing = false;
    private float showHideDelta = 0.0f;

    // Variables for the scrolling in animation
    private int scrollPosition = 0;
    private float scrollDelta = INITIAL_WAIT;
    private Boolean finishedMessage = true;
    private Boolean scrollMessage = false;
    private Boolean swipeConnected = false;

    public MessageBar() {
        messages = new ArrayList<Message>(MESSAGE_BUFFER);

        initTable();
        initCompactTable();
        initFullTable();

        this.add(compactView);

        Load();

        MessageManager.instance().addListener(this);
    }

    private void initTable() {
        this.setPosition(0, - COMPACT_HEIGHT);
        this.setHeight(COMPACT_HEIGHT);
        this.setWidth(Constants.VIEWPORT_WIDTH);
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
        compactText.setAlignment(Align.left);
        Image compactArrow = new Image(Assets.instance().ui_up_arrow_inverted);
        compactType = new Image();

        compactView.add(compactType).left().width(15);
        compactView.add(compactText).expand().fill().width(Constants.VIEWPORT_WIDTH - 40).pad(4);
        compactView.add(compactArrow).right();
    }

    private void initFullTable() {
        fullView = new Table();
        fullView.align(Align.topLeft);
        fullContainer = new VerticalGroup();
        fullContainer.align(Align.topLeft);
        fullScroller = new ScrollPane(fullContainer);
        fullScroller.setFadeScrollBars(false);
        fullScroller.setStyle(Constants.ScrollPaneStyleTerminal());

        fullView.add(fullScroller).expand().fill().bottom().prefWidth(GameStage.VIEWPORT_WIDTH - 4).padTop(2);
    }

    /**
     * Show the MessageBar if not visible already.
     * Plays a Notification sound if notify is true.
     *
     * @param notify Whether to play a notification sound
     */
    public void Show(boolean notify) {
        if (visible)
            return;

        isShowing = true;
        if(notify)
            AudioManager.instance().playNotificationSound();
    }

    /**
     * Toggle between compact and full mode.
     */
    public void ToggleView() {
        if (compact) {
            // We are now in Full mode!
            compact = false;

            this.clearChildren();
            int FULL_HEIGHT = 200;
            this.setHeight(FULL_HEIGHT);
            this.add(fullView);

            // scroll to bottom
            fullScroller.scrollTo(fullScroller.getX(), fullScroller.getY(), fullScroller.getWidth(), fullScroller.getHeight());
        }
        else {
            // We are now in compact mode!
            compact = true;

            this.clearChildren();
            this.setHeight(COMPACT_HEIGHT);
            this.add(compactView);

            // set the compact view to the newest message
            finishedMessage = true;
            compactPosition = messages.size() - 1;
            scrollPosition = 0;
            scrollDelta = 0;

            if (!messages.isEmpty()) {
                int textLength = messages.get(compactPosition).getText().length();
                compactType.setDrawable(Message.GetTypeIcon(messages.get(compactPosition)));

                if (textLength > SCROLLING_TEXT_CHARS) {
                    compactText.setText(messages.get(compactPosition).getText().substring(textLength - SCROLLING_TEXT_CHARS, textLength));
                    scrollPosition = textLength - SCROLLING_TEXT_CHARS;
                }
                else
                    compactText.setText(messages.get(compactPosition).getText());
            }
        }
    }

    private void NewMessage(Message newMe, boolean show) {
        if(show) Show(true);

        if (messages.size() > MESSAGE_BUFFER - 2) {
            messages.remove(0);
            compactPosition--;
        }

        messages.add(newMe);

        if (fullContainer.getChildren().size > MESSAGE_BUFFER - 2) {
            fullContainer.removeActor(fullContainer.getChildren().first());
        }

        fullContainer.addActor(new MessageUIElement(newMe));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // We need to add the swipe runnable here because in the constructor the gamescreen object is not ready yet
        if (!swipeConnected) {
            swipeConnected = true;

            ScreenManager.setSwipeUpAction(new Runnable() {
                @Override
                public void run() {
                    Show(false);
                }
            });
        }

        float ANIMATION_TIME = 0.5f;
        if (visible || isShowing) {
            Scroll(delta);
        }
        else if (isHiding) {
            showHideDelta += delta;

            float progress = Math.min(1f, showHideDelta/ ANIMATION_TIME);
            float interpol = Interpolation.sineOut.apply(progress);

            this.setPosition(0, -(COMPACT_HEIGHT * interpol));

            if (progress == 1f) {
                isHiding = false;
                visible = false;

                showHideDelta = 0;
            }
        }

        if (isShowing) {
            showHideDelta += delta;

            float progress = Math.min(1f, showHideDelta/ ANIMATION_TIME);
            float interpol = Interpolation.sineOut.apply(progress);

            this.setPosition(0, -COMPACT_HEIGHT + (COMPACT_HEIGHT * interpol));

            if (progress == 1f) {
                isShowing = false;
                visible = true;

                showHideDelta = 0;
                scrollDelta = 0;
            }
        }
    }

    @Override
    public void OnEvent(EventType type, Object sender) {
        if (type == EventType.MESSAGE_NEW) {
            NewMessage((Message) sender, true);
        }
    }

    private void Scroll(float delta) {
        // this is the scrolling mechanism
        // we only want to scroll if we are in the compact view and when the message is longer than the desired value
        if (compact) {
            scrollDelta += delta;

            int FINAL_WAIT = 2;
            if (scrollMessage) {
                // if the message is new we wait INITIAL_WAIT before scrolling
                if (scrollPosition == 0 && scrollDelta < INITIAL_WAIT) {
                    // just wait
                }
                // the initial wait is finished
                else {
                    // Here we control the scrolling speed
                    float SCROLLING_SPEED = 0.2f;
                    if (scrollDelta > SCROLLING_SPEED) {
                        scrollDelta = 0f;

                        // Are we at the end of the message?
                        if (messages.get(compactPosition).getText().length() > (scrollPosition + SCROLLING_TEXT_CHARS)) {
                            scrollPosition++;

                            // Set the actual displayed substring
                            compactText.setText(messages.get(compactPosition).getText().substring(scrollPosition, SCROLLING_TEXT_CHARS + scrollPosition));
                        }
                        else {
                            scrollMessage = false;
                            finishedMessage = true;
                        }
                    }
                }
            }
            else if (scrollDelta > FINAL_WAIT) {
                finishedMessage = true;
            }

            // if the message is finished we set the new message from the queue
            if (finishedMessage && compactPosition != (messages.size() - 1)) {
                if (scrollDelta > FINAL_WAIT || messages.size() == 1) {
                    finishedMessage = false;
                    compactPosition++;
                    scrollPosition = 0;
                    scrollDelta = 0;

                    compactType.setDrawable(Message.GetTypeIcon(messages.get(compactPosition)));

                    for (com.badlogic.gdx.scenes.scene2d.EventListener e : compactText.getListeners()) {
                        compactText.removeListener(e);
                    }

                    if (messages.get(compactPosition).getListener() != null) {
                        compactText.addListener(messages.get(compactPosition).getListener());
                    }

                    int textLength = messages.get(compactPosition).getText().length();

                    if (textLength > SCROLLING_TEXT_CHARS) {
                        scrollMessage = true;
                        compactText.setText(messages.get(compactPosition).getText().substring(0, SCROLLING_TEXT_CHARS));
                    }
                    else {
                        scrollMessage = false;
                        compactText.setText(messages.get(compactPosition).getText());
                    }
                }
            }
            else {
                if (visible && !isHiding) {
                    if (scrollDelta > FINAL_WAIT) {
                        isHiding = true;
                        visible = false;
                    }
                }
            }
        }
    }

    public Boolean getVisible() {
        return visible;
    }

    public Proto.MessageBar Save() {
        Proto.MessageBar.Builder builder = Proto.MessageBar.newBuilder();

        for (Message m: messages) {
            builder.addMessages(m.getData().build());
        }

        return builder.build();
    }

    public void Load() {
        if (SaveGameManager.getMessageBar() != null) {
            for (Proto.Message m:SaveGameManager.getMessageBar().getMessagesList()) {
                NewMessage(new Message(m.toBuilder()), false);

                // hacky but jumps to the last message for compact view
                ToggleView();
                ToggleView();
            }
        }
    }
}
