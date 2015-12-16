package com.polytech.spik.sms.discovery;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by momo- on 21/10/2015.
 */
public class LanDiscoveryServer implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanDiscoveryServer.class);

    private static final int DISCOVERY_PORT = 10101;

    private final Bootstrap bootstrap;
    private Channel channel;

    public LanDiscoveryServer(final EventLoopGroup eventLoop, final AbstractLanDiscoveryHandler handler){
        bootstrap = new Bootstrap()
            .group(eventLoop)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_BROADCAST, true)
            .handler(new ChannelInitializer<NioDatagramChannel>() {
                @Override
                protected void initChannel(NioDatagramChannel channel) throws Exception {
                final ChannelPipeline pipeline = channel.pipeline();

                if(System.getProperty("spik.network.debug", "false").equals("true")) {
                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                }

                pipeline.addLast(handler);
            }
        });

    }

    public ChannelFuture start() throws InterruptedException {
        LOGGER.info("Starting DiscoveryServer on port {}", DISCOVERY_PORT);

        channel = bootstrap.bind(DISCOVERY_PORT).sync().channel();
        return channel.closeFuture();
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing DiscoveryServer {}", channel);

        if(channel != null)
            channel.close();
    }
}
