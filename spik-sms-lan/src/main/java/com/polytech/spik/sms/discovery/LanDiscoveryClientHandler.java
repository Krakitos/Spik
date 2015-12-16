package com.polytech.spik.sms.discovery;

import com.polytech.spik.domain.Computer;
import com.polytech.spik.protocol.DiscoveryMessages;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfuntowicz on 24/10/15.
 */
public class LanDiscoveryClientHandler extends AbstractLanDiscoveryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanDiscoveryClientHandler.class);

    private final LanDiscoveryClientCallback callback;
    private List<Computer> computers;

    public LanDiscoveryClientHandler(LanDiscoveryClientCallback callback){
        this.callback = callback;
        this.computers = new ArrayList<>();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            ctx.executor().submit(new Runnable() {
                @Override
                public void run() {
                    callback.onDiscoveryDone(computers);
                }
            });
        }
    }

    @Override
    protected void handleDiscoveryResponse(ChannelHandlerContext ctx, InetSocketAddress sender, DiscoveryMessages.DiscoveryResponse response) {
        final Computer phone = new Computer(
            response.getName(),
            response.getOs().name(),
            response.getVersion(),
            sender.getHostString(),
            response.getPort()
        );

        LOGGER.info("Received DiscoveryResponse from {}", phone);
        computers.add(phone);
    }
}
