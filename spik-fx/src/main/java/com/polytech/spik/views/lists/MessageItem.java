package com.polytech.spik.views.lists;

import com.polytech.spik.domain.FXMessage;
import com.polytech.spik.domain.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

/**
 * Created by momo- on 04/11/2015.
 */
public class MessageItem extends ListCell<FXMessage> {

    private static final DateTimeFormatter TODAY_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

    private static final Pos SENT_MESSAGE = Pos.CENTER_LEFT;
    private static final Pos RECEIVED_MESSAGE = Pos.CENTER_RIGHT;

    private Label messageText;
    private Label messageDate;
    private VBox container;

    public MessageItem(){
        messageText = new Label();
        messageDate = new Label();

        messageText.maxWidthProperty().bind(widthProperty().divide(2).add(20));
        messageText.prefWidth(-1);
        messageText.setWrapText(true);
        messageText.setPadding(new Insets(3, 15, 3, 15));

        messageDate.setAlignment(Pos.BOTTOM_RIGHT);
        messageDate.setTextAlignment(TextAlignment.RIGHT);

        VBox.setVgrow(messageText, Priority.NEVER);

        container = new VBox(3, messageText, messageDate);

        setGraphic(new Group(container));
    }

    @Override
    protected void updateItem(FXMessage item, boolean empty) {
        super.updateItem(item, empty);

        messageText.setVisible(!empty);
        messageText.getStyleClass().clear();

        if (empty || item == null){
            messageText.setText(null);
            messageDate.setText(null);
        }else {
            final LocalDateTime dateReceived = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(item.date()), ZoneId.systemDefault()
            );

            if(item.status() == Message.Status.SENT){
                setAlignment(SENT_MESSAGE);
                messageText.getStyleClass().add("message_sent");
            }else{
                setAlignment(RECEIVED_MESSAGE);
                messageText.getStyleClass().add("message_received");
            }

            messageText.setText(item.text());

            if(dateReceived.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
                messageDate.setText(dateReceived.format(DEFAULT_FORMATTER));
            }else{
                messageDate.setText(dateReceived.format(TODAY_FORMATTER));
            }
        }

    }
}
