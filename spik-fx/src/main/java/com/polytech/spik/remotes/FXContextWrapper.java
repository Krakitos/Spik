package com.polytech.spik.remotes;

import com.polytech.spik.domain.Contact;

/**
 * Created by momo- on 19/12/2015.
 */
public interface FXContextWrapper {
    FXContext fxContext();

    void sendMessage(Iterable<Contact> participants, String text);
}
