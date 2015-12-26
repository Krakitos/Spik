package com.polytech.spik.domain;

/**
 * Created by momo- on 19/12/2015.
 */
public interface Contact {

    /**
     * Contact's id
     * @return
     */
    long id();

    /**
     * Contact's name
     * @return
     */
    String name();

    /**
     * Contact's address
     * @return
     */
    String address();

    /**
     * Contact's picture
     * @return Might be null if no photo available
     * @see #hasPicture()
     */
    byte[] picture();

    /**
     * Indicate if the contact has a photo
     * @return True if a picture is available, false otherwise
     */
    boolean hasPicture();
}
