package com.polytech.spik.sms.discovery;

import com.polytech.spik.domain.Computer;
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

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by mfuntowicz on 24/10/15.
 */
public class LanDiscoveryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanDiscoveryClient.class);

    private static final int DEFAULT_PORT = 10101;

    private final EventLoopGroup loopGroup;

    public LanDiscoveryClient(){
        this(new NioEventLoopGroup(1));
    }

    public LanDiscoveryClient(final EventLoopGroup loopGroup){
        this.loopGroup = loopGroup;
    }

    public List<Computer> discover(String broadcastIp, Phone phone) {
        ByteBuf raw = createMessage(phone);
        LanDiscoveryClientHandler handler = new LanDiscoveryClientHandler();
        InetSocketAddress broadcastSocketIp = new InetSocketAddress(broadcastIp, DEFAULT_PORT);

        LOGGER.info("Sending Broadcast discovery to: {}", broadcastSocketIp);

        Channel channel = null;
        try {
            channel = createBootstrap(handler).bind(0).syncUninterruptibly().channel();
            channel.writeAndFlush(new DatagramPacket(raw, broadcastSocketIp));
            channel.closeFuture().sync();
        }catch (InterruptedException e){
            LOGGER.warn("Discovery process interrupted", e);
        }finally {
            if(channel != null) {
                if (channel.isOpen())
                    channel.closeFuture().awaitUninterruptibly();
            }
        }

        return handler.computers();
    }

    private ByteBuf createMessage(Phone phone){
        final DiscoveryMessages.DiscoveryRequest request =
                DiscoveryMessages.DiscoveryRequest.newBuilder()
                .setName(phone.name())
                .setManufacturer(phone.manufacturer())
                .setModel(phone.model())
                .setOs(DiscoveryMessages.OperatingSystem.valueOf(phone.os().toUpperCase()))
                .setSdkVersion(phone.sdkVersion())
                .build();

        final DiscoveryMessages.DiscoveryMessage msg = DiscoveryMessages.DiscoveryMessage.newBuilder()
                .setRequest(request)
                .build();

        return Unpooled.wrappedBuffer(msg.toByteArray());
    }

    private Bootstrap createBootstrap(final LanDiscoveryClientHandler handler){
        return new Bootstrap()
            .group(loopGroup)
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
                    pipeline.addLast(handler);
                }
            });
    }
}
