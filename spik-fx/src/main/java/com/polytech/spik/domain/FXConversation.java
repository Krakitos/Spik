package com.polytech.spik.domain;

import com.guigarage.controls.Media;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXConversation implements Conversation, Media {

    private static final int SNIPPET_LENGTH = 40;
    private static final Image EMPTY_CONTACT =
            new Image(FXConversation.class.getClassLoader().getResourceAsStream("images/ic_account_circle_black_48dp_2x.png"));

    public static Comparator<FXMessage> DEFAULT_COMPARATOR = new MessageComparator();

    private SimpleLongProperty id;
    private ObservableList<Contact> participants;
    private ObservableList<FXMessage> messages;
    private SortedList<FXMessage> sortedMessages;
    private SimpleStringProperty title;
    private SimpleStringProperty snippet;
    private SimpleObjectProperty<Image> image;

    public FXConversation(long id, Collection<Contact> participants) {
        this(id, participants, new ArrayList<>());
    }

    public FXConversation(long id, Collection<Contact> participants, Collection<FXMessage> messages) {
        this.id = new SimpleLongProperty(id);
        this.participants = FXCollections.observableArrayList(participants);
        this.messages = FXCollections.observableArrayList(messages);
        this.sortedMessages = this.messages.sorted(DEFAULT_COMPARATOR);

        this.snippet = new SimpleStringProperty();
        this.sortedMessages.addListener((InvalidationListener) c -> updateSnippet());

        updateSnippet();
    }

    public long id(){
         return id.get();
    }

    public ReadOnlyLongProperty idProperty(){
        return id;
    }

    public Iterable<Contact> participants(){
        return participants;
    }

    public ObservableList<Contact> participantsProperty(){
        return FXCollections.unmodifiableObservableList(participants);
    }

    public Collection<Message> messages(){
        return messages.stream().collect(Collectors.toList());
    }

    @Override
    public void addMessage(Message message) {
        if(message instanceof FXMessage)
            messages.add(((FXMessage) message));
    }

    public ObservableList<FXMessage> messagesProperty(){
        return messages;
    }

    public SortedList<FXMessage> sortedMessagesProperty(){ return sortedMessages; }

    @Override
    public long lastMessageDate() {
        return sortedMessages.get(sortedMessages.size() - 1).date();
    }

    public String snippet() {
        return snippet.get();
    }

    public SimpleStringProperty snippetProperty() {
        return snippet;
    }

    private void updateSnippet() {
        if(messages.size() > 0){
            String msg = sortedMessages.get(sortedMessages.size() - 1).text();
            snippet.set(msg.length() < SNIPPET_LENGTH ? msg : msg.substring(0, SNIPPET_LENGTH));
        }
    }

    @Override
    public StringProperty titleProperty() {
        if(Objects.isNull(title)){

            //Compute name1, name2, ...
            List<String> names = participants.stream()
                    .map(Contact::name)
                    .collect(Collectors.toList());

            this.title = new SimpleStringProperty(String.join(", ", names));
        }
        return title;
    }

    @Override
    public StringProperty descriptionProperty() {
        return snippet;
    }

    @Override
    public ObjectProperty<Image> imageProperty() {
        if(Objects.isNull(image)) {
            Image toDisplay = participants.stream()
                    .filter(Contact::hasPicture)
                    .findAny()
                    .map(c -> new Image(new ByteArrayInputStream(c.picture())))
                    .orElse(EMPTY_CONTACT);

            image = new SimpleObjectProperty<>(toDisplay);
        }

        return image;
    }

    public static class MessageComparator implements Comparator<FXMessage>{
        @Override
        public int compare(FXMessage a, FXMessage b) {
            return Long.compareUnsigned(a.date(), b.date());
        }
    }
}
