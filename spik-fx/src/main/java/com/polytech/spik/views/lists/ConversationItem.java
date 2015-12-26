package com.polytech.spik.views.lists;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.FXConversation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by momo- on 28/10/2015.
 */
public class ConversationItem extends ListCell<FXConversation> {

    private static final Image EMPTY_CONTACT =
            new Image(ConversationItem.class.getClassLoader().getResourceAsStream("images/ic_person_black_48dp.png"), 30, 30, true, true);

    private final HBox rootContainer;
    private final VBox snippetContainer;
    private final ImageView contactImg;
    private final Label contactName;
    private final Label conversationSnippet;

    public ConversationItem() {
        contactImg = new ImageView();
        contactName = new Label();
        conversationSnippet = new Label();

        snippetContainer = new VBox(contactName, conversationSnippet);
        rootContainer = new HBox(contactImg, snippetContainer);
        rootContainer.setPrefHeight(40);

        rootContainer.setAlignment(Pos.CENTER_LEFT);
        snippetContainer.setAlignment(Pos.CENTER_LEFT);

        VBox.setVgrow(snippetContainer, Priority.ALWAYS);

        setGraphic(rootContainer);
    }

    @Override
    protected void updateItem(FXConversation item, boolean empty) {
        super.updateItem(item, empty);

        if(Objects.nonNull(item)){
            if(item.participantsProperty().size() == 1){
                /*if(item.participants().get(0).hasPicture()) {
                    Image pic = new Image(new ByteArrayInputStream(item.participants().get(0).picture()), 30, 30, true, true);
                    contactImg.setImage(pic);
                }else{
                    contactImg.setImage(EMPTY_CONTACT);
                }*/

                contactImg.setImage(EMPTY_CONTACT);
            }

            contactName.setText(item.participantsProperty().stream().map(Contact::name).collect(Collectors.joining(", ")));
            conversationSnippet.textProperty().bind(item.snippetProperty());
        }else
            hide();

    }

    private void hide() {
        contactImg.setImage(null);
        contactName.setText("");
        conversationSnippet.textProperty().unbind();
        conversationSnippet.setText("");
    }
}
