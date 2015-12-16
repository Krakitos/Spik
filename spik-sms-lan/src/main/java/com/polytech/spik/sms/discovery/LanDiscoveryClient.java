package com.polytech.spik.sms.discovery;

import com.polytech.spik.domain.Phone;
import com.polytech.spik.protocol.DiscoveryMessages;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by mfuntowicz on 24/10/15.
 */
public class LanDiscoveryClient implements Closeable{

    private static final Logger LOGGER = LoggerFactory.getLogger(LanDiscoveryClient.class);

    private static final InetSocketAddress DISCOVERY_ADDRESS =
            new InetSocketAddress("255.255.255.255", 10101);

    private final LanDiscoveryClientCallback callback;
    private final Bootstrap bootstrap;
    private Channel channel;

    public LanDiscoveryClient(final LanDiscoveryClientCallback callback){
        this(new NioEventLoopGroup(), callback);
    }

    public LanDiscoveryClient(final EventLoopGroup group, final LanDiscoveryClientCallback callback){
        this.callback = callback;
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        final ChannelPipeline pipeline = ch.pipeline();

                        if(System.getProperty("spik.network.debug", "false").equals("true")) {
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }

                        pipeline.addLast(new ReadTimeoutHandler(2));
                        pipeline.addLast(new LanDiscoveryClientHandler(callback));
                    }
                });
    }

    public void connect() throws InterruptedException {
        LOGGER.debug("Binding the UDP Socket");
        channel = bootstrap.bind(0).sync().channel();
    }

    public void sendDiscoveryRequest(Phone phone) throws InterruptedException {
        final DiscoveryMessages.DiscoveryRequest request = DiscoveryMessages.DiscoveryRequest.newBuilder()
                .setName(phone.name())
                .setManufacturer(phone.manufacturer())
                .setModel(phone.model())
                .setOs(DiscoveryMessages.OperatingSystem.valueOf(phone.os().toUpperCase()))
                .setSdkVersion(phone.sdkVersion())
                .build();

        final DiscoveryMessages.DiscoveryMessage msg = DiscoveryMessages.DiscoveryMessage.newBuilder()
                .setRequest(request)
                .build();

        final ByteBuf raw = Unpooled.wrappedBuffer(msg.toByteArray());

        LOGGER.trace("Sending discovery request");

        callback.onDiscoveryStarted();

        channel.writeAndFlush(new DatagramPacket(raw, DISCOVERY_ADDRESS));

        channel.closeFuture().sync();
    }

    @Override
    public void close() throws IOException {
        if(channel != null)
            channel.close().awaitUninterruptibly();
    }
}
