package com.polytech.spik.controllers;

import com.guigarage.controls.Emoji;
import com.guigarage.controls.EmojiUtil;
import com.guigarage.controls.SimpleMediaListCell;
import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.FXConversation;
import com.polytech.spik.domain.FXEventHandler;
import com.polytech.spik.domain.FXMessage;
import com.polytech.spik.remotes.FXContext;
import com.polytech.spik.remotes.FXContextWrapper;
import com.polytech.spik.services.sms.LanSmsService;
import com.polytech.spik.services.sms.SmsContext;
import com.polytech.spik.views.lists.MessageItem;
import com.polytech.spik.views.notifications.NotificationManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.spreadsheet.StringConverterWithFormat;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by momo- on 15/12/2015.
 */
public class MainController implements Initializable, FXEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private static final Image COMPOSE_IMG =
            new Image(MainController.class.getClassLoader().getResourceAsStream("images/ic_compose_white_48dp_2x.png"), 36, 36, false, true);

    private static final Image NEW_CONVERSATION_IMG =
            new Image(MainController.class.getClassLoader().getResourceAsStream("images/ic_add_white_48dp_2x.png"), 36, 36, false, true);


    /** User Interface Bindings **/
    public TextField message_input;
    public ListView<FXConversation> conversations_list;
    public ListView<FXMessage> messages_list;
    public Label participants_label;
    public TextField participants_input;
    public Button emojiDisplayer;
    public Button new_conversation;

    public ImageView new_conversation_img;
    final Node emojiGrid = EmojiUtil.createEmoticonsPane(emoji -> message_input.appendText(emoji.getEmoji().getEmoji()));
    final Popup emojiPopup = new Popup();

    /** Model **/
    private ResourceBundle resources;
    private ObservableList<FXConversation> conversations;

    /** Services **/
    private LanSmsService smsService;
    private List<FXContextWrapper> contexts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.trace("Initialized Spik with Bundle {}", resources.getLocale());

        this.resources = resources;
        this.conversations = FXCollections.observableArrayList();
        this.contexts = new ArrayList<>();

        setupUi();
        launchSpik();
    }

    private void setupUi(){
        conversations_list.setCellFactory(param -> {
            SimpleMediaListCell<FXConversation> cell = new SimpleMediaListCell<>();
            cell.getTitleLabel().getStyleClass().clear();
            cell.getTitleLabel().setStyle("-fx-font-size: 16; -fx-font-weight: 600");

            cell.getDescriptionLabel().getStyleClass().clear();
            cell.getDescriptionLabel().setStyle("-fx-font-size: 14; -fx-fill: darkgray; -fx-text-overrun: ellipsis");
            return cell;
        });
        messages_list.setCellFactory(param -> new MessageItem());

        conversations_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                updateParticipants(newValue.participants());
                messages_list.setItems(newValue.sortedMessagesProperty());
                messages_list.scrollTo(newValue.messagesProperty().size());
            }
        });

        participants_input.visibleProperty().bind(participants_label.visibleProperty().not());

        //Setup Emoji pane
        emojiDisplayer.setText(Emoji.E_0000.getEmoji());
        emojiPopup.setConsumeAutoHidingEvents(false);
        emojiPopup.setAutoHide(true);
        emojiPopup.getContent().add(emojiGrid);

        emojiPopup.setOnShown(ev -> {
            Bounds bounds = message_input.localToScreen(message_input.getBoundsInLocal());
            double emojiWidth = emojiPopup.getWidth();
            double emojiHeight = emojiPopup.getHeight();

            System.out.println(emojiWidth + " " + emojiHeight);

            double x = bounds.getMinX() + emojiWidth/2;
            double y = bounds.getMinY() - emojiHeight;

            emojiPopup.setAnchorX(x);
            emojiPopup.setAnchorY(y);
        });

        new_conversation_img.setImage(NEW_CONVERSATION_IMG);
        new_conversation.setOnMouseEntered(e -> {
            RotateTransition rotate = new RotateTransition(Duration.millis(200), new_conversation);
            rotate.setByAngle(360);
            rotate.setAutoReverse(false);
            rotate.setOnFinished(e2 -> new_conversation_img.setImage(COMPOSE_IMG));
            rotate.setAutoReverse(true);
            rotate.playFromStart();
        });

        new_conversation.setOnMouseExited(e -> {
            RotateTransition rotate = new RotateTransition(Duration.millis(200), new_conversation);
            rotate.setByAngle(-360);
            rotate.setAutoReverse(false);
            rotate.setOnFinished(e2 -> {
                new_conversation_img.setImage(NEW_CONVERSATION_IMG);

                if(new_conversation.getRotate() != 0)
                    new_conversation.setRotate(0);
            });
            rotate.setAutoReverse(true);
            rotate.playFromStart();
        });
    }

    private void cleanUi(FXContext fxContext){
        if(Platform.isFxApplicationThread()){
            LOGGER.info("Cleaning UI for context {}", fxContext);

            fxContext.clear();
            messages_list.setItems(null);
            participants_label.setText(null);
        }else{
            Platform.runLater(() -> cleanUi(fxContext));
        }
    }

    private void updateParticipants(Iterable<Contact> participants) {
        final List<String> address = StreamSupport.stream(participants.spliterator(), true)
                .map(Contact::name)
                .collect(Collectors.toList());

        final String label = String.join(", ", address);
        participants_label.setText(label);

        if(!participants_label.isVisible())
            participants_label.setVisible(true);
    }

    private void launchSpik() {
        try {
            smsService = new LanSmsService(() -> {
                SmsContext context = new SmsContext(new FXContext(), this){

                    @Override
                    protected void onConnected() {
                        showNotification(
                            resources.getString("spik.sms.connection"),
                            resources.getString("spik.sms.device_connected")
                        );
                    }

                    @Override
                    protected void onDisconnected() {
                        LOGGER.info("Device disconnected");
                        cleanUi(fxContext());

                        showNotification(
                            resources.getString("spik.sms.disconnection"),
                            resources.getString("spik.sms.device_disconnected")
                        );
                    }
                };

                contexts.add(context);

                LOGGER.trace("Registered new context {}", context);

                conversations_list.setItems(
                    context.fxContext()
                    .conversationsProperty()
                    .sorted((a, b) -> -Long.compareUnsigned(a.lastMessageDate(), b.lastMessageDate()))
                );

                return context;
            });
            smsService.run();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to launch Sms Service", e);
        }
    }

    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER && !message_input.getText().isEmpty()){
            LOGGER.trace("Click on send button (message.size = {})", message_input.getText().length());
            FXConversation selectedItem = conversations_list.getSelectionModel().getSelectedItem();

            Optional<FXContextWrapper> context = contexts.stream()
                    .filter(c -> c.fxContext().conversationsProperty().contains(selectedItem))
                    .findFirst();

            if(context.isPresent()){
                context.get().sendMessage(selectedItem.participants(), message_input.getText());
                message_input.clear();
            }else{
                LOGGER.error("Unable to find context to sendMessage");

                Alert alert = new Alert(Alert.AlertType.ERROR, resources.getString("spik.sms.unable_to_send"), ButtonType.CLOSE);
                alert.show();
            }
        }
    }

    @Override
    public void onMessageReceived(FXConversation c, FXMessage message) {
        String partipants = String.join(
                ", ",
                c.participantsProperty().stream().map(Contact::name).collect(Collectors.toList())
        );

        showNotification(
            String.format(resources.getString("spik.sms.message_received_title"), partipants),
            message.text().substring(0, Math.min(50, message.text().length()))
        );
    }

    private void showNotification(String title, String content){
        if(!Platform.isFxApplicationThread()){
            LOGGER.warn("Trying to show notification on non UI Thread");
            Platform.runLater(() -> showNotification(title, content));
        }else {
            LOGGER.trace("Showing notification : {}", title);

            NotificationManager.getInstance().provider().notify("Spik", title, content);
        }
    }

    public void onEmojiDisplayerClick(ActionEvent e) {

        if(!emojiPopup.isShowing())
            emojiPopup.show(emojiDisplayer, 0, 0);
    }

    public void onCreateConversation(Event event) {
        LOGGER.debug("Clicked on Create Conversation Btn");

        participants_label.setVisible(false);

    }

    public void onClick(Event event) {
        LOGGER.debug("Clicked on {}", event);
    }
}
