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

import java.util.ArrayList;
import java.util.List;

import de.hsd.hacking.Assets.Assets;
import de.hsd.hacking.Data.EventListener;
import de.hsd.hacking.Data.EventSender;
import de.hsd.hacking.Data.Messaging.Message;
import de.hsd.hacking.Data.Messaging.MessageManager;
import de.hsd.hacking.Stages.GameStage;
import de.hsd.hacking.Utils.Constants;

/**
 * UI element to display messages for the player.
 * @author Julian Geywitz
 */
public class MessageBar extends Table implements EventListener{
    private final int COMPACT_HEIGHT = 21;
    private final int FULL_HEIGHT = 200;
    private final int SCROLLING_TEXT_CHARS = 47;
    private final float SCROLLING_SPEED = 0.2f;
    private final int INITIAL_WAIT = 1;
    private final int FINAL_WAIT = 2;
    private final int MESSAGE_BUFFER = 100;

    private Table compactView;
    private Label compactText;
    private Image compactType;
    private Image compactArrow;

    private List<Message> messages;
    private int compactPosition = -1;

    private Table fullView;
    private ScrollPane fullScroller;
    private VerticalGroup fullContainer;

    private Boolean compact = true;

    // Variables for the scrolling in animation
    private int scrollPosition = 0;
    private float scrollDelta = 0f;
    private Boolean finishedMessage = true;
    private Boolean scrollMessage = false;

    public MessageBar() {
        messages = new ArrayList<Message>(MESSAGE_BUFFER);

        initTable();
        initCompactTable();
        initFullTable();

        this.add(compactView);

        MessageManager.instance().addListener(this);
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
        compactText.setAlignment(Align.left);
        compactArrow = new Image(Assets.instance().ui_up_arrow_inverted);
        compactType = new Image();

        compactView.add(compactType).left();
        compactView.add(compactText).expand().fill().width(GameStage.VIEWPORT_WIDTH - 40).pad(4);
        compactView.add(compactArrow).right();
    }

    private void initFullTable() {
        fullView = new Table();
        fullView.align(Align.topLeft);
        fullContainer = new VerticalGroup();
        fullContainer.align(Align.topLeft);
        fullScroller = new ScrollPane(fullContainer);

        fullView.add(fullScroller).expand().fill().bottom();
    }

    public void ToggleView() {
        if (compact) {
            // We are now in Full mode!
            compact = false;

            this.clearChildren();
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

            int textLength = messages.get(compactPosition).getText().length();
            compactType.setDrawable(Message.GetTypeIcon(messages.get(compactPosition)));

            if (textLength > SCROLLING_TEXT_CHARS)
                compactText.setText(messages.get(compactPosition).getText().substring(textLength - SCROLLING_TEXT_CHARS, textLength));
            else
                compactText.setText(messages.get(compactPosition).getText());
        }
    }

    private void NewMessage(Message newMe) {
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

        // this is the scrolling mechanism
        // we only want to scroll if we are in the compact view and when the message is longer than the desired value
        if (compact) {
            scrollDelta += delta;

            if (scrollMessage) {
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

            if (finishedMessage && compactPosition != (messages.size() - 1)) {
                if (scrollDelta > FINAL_WAIT) {
                    finishedMessage = false;
                    compactPosition++;
                    scrollPosition = 0;
                    scrollDelta = 0;

                    compactType.setDrawable(Message.GetTypeIcon(messages.get(compactPosition)));
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
        }

    }

    @Override
    public void OnEvent(EventType type, Object sender) {
        if (type == EventType.MESSAGE_NEW) {
            NewMessage((Message) sender);
        }
    }
}
