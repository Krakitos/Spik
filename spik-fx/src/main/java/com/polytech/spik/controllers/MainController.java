package com.polytech.spik.controllers;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.Message;
import com.polytech.spik.services.sms.LanSmsService;
import com.polytech.spik.services.sms.SmsRemoteContext;
import com.polytech.spik.views.lists.ConversationItem;
import com.polytech.spik.views.lists.MessageItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by momo- on 15/12/2015.
 */
public class MainController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);


    /** User Interface Bindings **/
    public TextField message_input;
    public ListView<Conversation> conversations_list;
    public HBox message_container;
    public ListView<Message> messages_list;
    public Button send_btn;
    public Label participants_label;

    /** Model **/
    private ResourceBundle resources;
    private ObservableList<Conversation> conversations;

    /** Services **/
    private LanSmsService smsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.trace("Initialized Spik with Bundle {}", resources.getLocale());

        this.resources = resources;
        this.conversations = FXCollections.observableArrayList();

        setupUi();
        launchSpik();
    }

    private void setupUi(){
        send_btn.disableProperty().bind(message_input.textProperty().isEmpty());

        conversations_list.setCellFactory(param -> new ConversationItem());
        messages_list.setCellFactory(param -> new MessageItem());

        conversations_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                updateParticipants(newValue.participants());
                messages_list.setItems(newValue.messagesProperty());
                messages_list.scrollTo(newValue.messagesProperty().size());
            }
        });

    }

    private void updateParticipants(Collection<Contact> participants) {
        final String label = String.join(", ", participants.stream().map(Contact::name).collect(Collectors.toList()));
        participants_label.setText(label);
    }

    private void launchSpik() {
        try {
            smsService = new LanSmsService(() -> {
                SmsRemoteContext context = new SmsRemoteContext();
                conversations_list.setItems(context.conversationsProperty());
                return context;
            });
            smsService.run();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to launch Sms Service", e);
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        LOGGER.trace("Click on send button (message.size = {})", message_input.getText().length());


    }
}
