package com.polytech.spik.services.sms;

import com.polytech.spik.sms.SmsHandlerFactory;
import com.polytech.spik.sms.discovery.LanDiscoveryServer;
import com.polytech.spik.sms.service.LanSmsServer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by momo- on 15/12/2015.
 */
public class LanSmsService {

    private EventLoopGroup eventLoopGroup;
    private LanSmsServer lanSmsServer;
    private LanDiscoveryServer lanDiscoveryServer;

    public LanSmsService(SmsHandlerFactory handlerFactory) {
        eventLoopGroup = new NioEventLoopGroup(2, Executors.newFixedThreadPool(2));
        lanSmsServer = new LanSmsServer(eventLoopGroup, handlerFactory);
        lanDiscoveryServer = new LanDiscoveryServer(eventLoopGroup, null);
    }

    public void run() throws InterruptedException {
        lanSmsServer.start();
    }

    public void stop() throws IOException {
        lanSmsServer.close();
    }
}
