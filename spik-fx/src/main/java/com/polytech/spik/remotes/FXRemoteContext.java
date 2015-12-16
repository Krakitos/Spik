package com.polytech.spik.remotes;

import com.polytech.spik.domain.Conversation;
import javafx.collections.ObservableList;

/**
 * Created by momo- on 16/12/2015.
 */
public interface FXRemoteContext extends RemoteContext {

    ObservableList<Conversation> conversationsProperty();
}
