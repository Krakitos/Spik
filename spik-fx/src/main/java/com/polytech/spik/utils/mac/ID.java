package com.polytech.spik.utils.mac;

import com.sun.jna.NativeLong;
/**
 * Could be an address in memory (if pointer to a class or method) or a value (like 0 or 1)
 *
 * Created by mfuntowicz on 30/12/15.
 */
public class ID extends NativeLong {
    public static final ID NIL = new ID(0);
    public ID() {
    }

    public ID(long peer) {
        super(peer);
    }
}
