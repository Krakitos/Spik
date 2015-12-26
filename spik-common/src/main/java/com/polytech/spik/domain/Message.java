package com.polytech.spik.domain;

/**
 * Created by momo- on 19/12/2015.
 */
public interface Message {

    /**
     * Message's id
     * @return
     */
    long id();

    /**
     * Date of this message
     * @return
     */
    long date();

    /**
     * Message's text content, if any
     * @return
     */
    String text();

    /**
     * Multimedia content of the message
     * Might be null
     * @return
     * @see #hasContent()
     */
    byte[] content();

    /**
     * Indicate if this message contains multimedia part
     * @return
     */
    boolean hasContent();

    /**
     * Mime's of the content
     * @return
     * @see #hasContent
     */
    String contentMime();

    /**
     * Message's status
     * @return
     */
    Status status();

    enum Status {
        READ,NOT_READ, PENDING, SENT
    }
}
