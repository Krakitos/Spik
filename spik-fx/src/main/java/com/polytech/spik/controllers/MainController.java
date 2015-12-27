package com.polytech.spik.controllers;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.FXConversation;
import com.polytech.spik.domain.FXMessage;
import com.polytech.spik.remotes.FXContext;
import com.polytech.spik.remotes.FXContextWrapper;
import com.polytech.spik.services.sms.LanSmsService;
import com.polytech.spik.services.sms.SmsContext;
import com.polytech.spik.views.lists.ConversationItem;
import com.polytech.spik.views.lists.MessageItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by momo- on 15/12/2015.
 */
public class MainController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    /** User Interface Bindings **/
    public TextField message_input;
    public ListView<FXConversation> conversations_list;
    public ListView<FXMessage> messages_list;
    public Label participants_label;

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
        conversations_list.setCellFactory(param -> new ConversationItem());
        messages_list.setCellFactory(param -> new MessageItem());

        conversations_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                updateParticipants(newValue.participants());
                messages_list.setItems(newValue.sortedMessagesProperty());
                messages_list.scrollTo(newValue.messagesProperty().size());
            }
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
                SmsContext context = new SmsContext(new FXContext()){

                    @Override
                    protected void onConnected() {

                    }

                    @Override
                    protected void onDisconnected() {
                        LOGGER.info("Device disconnected");
                        cleanUi(fxContext());
                    }
                };

                contexts.add(context);

                LOGGER.trace("Registered new context {}", context);

                conversations_list.setItems(context.fxContext().conversationsProperty());
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
}
