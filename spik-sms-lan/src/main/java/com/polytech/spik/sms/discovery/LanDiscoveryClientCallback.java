package com.polytech.spik.sms.discovery;


import com.polytech.spik.domain.Computer;

import java.util.Collection;

/**
 * Created by mfuntowicz on 24/10/15.
 */
public interface LanDiscoveryClientCallback {

    void onDiscoveryStarted();

    /**
     * Called when the discovery process is over
     * @param phones List all the phone available to connect onto
     */
    void onDiscoveryDone(Collection<Computer> phones);
}
