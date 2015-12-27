package com.polytech.spik.views.lists;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.FXConversation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineJoin;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by momo- on 28/10/2015.
 */
public class ConversationItem extends ListCell<FXConversation> {

    private static final Image EMPTY_CONTACT =
            new Image(ConversationItem.class.getClassLoader().getResourceAsStream("images/ic_person_black_48dp.png"), 192, 192, true, true);

    private static final ImagePattern EMPTY_CONTACT_PATTERN = new ImagePattern(EMPTY_CONTACT, 0, 0, 1, 1, true);

    private final HBox rootContainer;
    private final VBox snippetContainer;
    private final Circle contactImg;
    private final Label contactName;
    private final Label conversationSnippet;

    public ConversationItem() {
        contactImg = new Circle(24);
        contactName = new Label();
        conversationSnippet = new Label();

        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox.setVgrow(separator, Priority.ALWAYS);

        contactImg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        contactImg.setStrokeWidth(1);
        contactImg.setStroke(Color.WHITE);

        snippetContainer = new VBox(2, contactName, conversationSnippet);
        rootContainer = new HBox(20, contactImg, snippetContainer);
        rootContainer.setPrefHeight(50);

        rootContainer.setAlignment(Pos.CENTER_LEFT);
        snippetContainer.setAlignment(Pos.CENTER_LEFT);

        VBox.setVgrow(snippetContainer, Priority.ALWAYS);

        setGraphic(new VBox(5, rootContainer, separator));
    }

    @Override
    protected void updateItem(FXConversation item, boolean empty) {
        super.updateItem(item, empty);

        if(Objects.nonNull(item)){
            /*Optional<Contact> toDisplay = StreamSupport.stream(item.participants().spliterator(), false)
                    .filter(Contact::hasPicture)
                    .findAny();*/


            ImagePattern pattern = EMPTY_CONTACT_PATTERN;

            /*if(toDisplay.isPresent()) {
                Image pic = new Image(new ByteArrayInputStream(toDisplay.get().picture()), 64, 64, true, true);
                pattern = new ImagePattern(pic, 0, 0, 1, 1, true);
            }*/

            contactImg.setStroke(Color.LIGHTGRAY);
            contactImg.setFill(pattern);

            contactName.setText(item.participantsProperty().stream().map(Contact::name).collect(Collectors.joining(", ")));
            conversationSnippet.textProperty().bind(item.snippetProperty());
            conversationSnippet.setTextOverrun(OverrunStyle.ELLIPSIS);
        }else
            hide();

    }

    private void hide() {
        contactImg.setFill(null);
        contactImg.setStroke(Color.WHITE);

        contactName.setText("");
        conversationSnippet.textProperty().unbind();
        conversationSnippet.setText("");
    }
}
