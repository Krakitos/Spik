package com.polytech.spik.sms.service;

import com.polytech.spik.domain.Phone;
import com.polytech.spik.exceptions.UnboundServerException;
import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.sms.filters.DynamicIpFilter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.compression.SnappyFrameDecoder;
import io.netty.handler.codec.compression.SnappyFrameEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.ipfilter.UniqueIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by mfuntowicz on 14/12/15.
 */
public class LanSmsServer implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsServer.class);

    private ServerBootstrap bootstrap;
    private Channel channel;

    private Set<Phone> authorizedPhones;
    private Timer authorizedPhonesTimeout;

    public LanSmsServer(EventLoopGroup eventLoopGroup, final HandlerFactory factory) {
        authorizedPhones = new HashSet<>();
        authorizedPhonesTimeout = new Timer("Phone Timeout", false);

        bootstrap = new ServerBootstrap()
            .group(eventLoopGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    final ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast(new DynamicIpFilter(authorizedPhones));
                    pipeline.addLast(new UniqueIpFilter());
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1 << 16, 0, 2, 0, 2));
                    pipeline.addLast(new LengthFieldPrepender(2));
                    pipeline.addLast(new SnappyFrameDecoder());
                    pipeline.addLast(new SnappyFrameEncoder());
                    pipeline.addLast(new ProtobufDecoder(SpikMessages.Wrapper.getDefaultInstance()));
                    pipeline.addLast(new ProtobufEncoder());
                    pipeline.addLast(factory.newInstance());

                    if(System.getProperty("spik.network.debug", "false").equals("true")) {
                        pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                    }
                }
            });
    }

    public void start() throws InterruptedException {
        LOGGER.info("Binding the server");

        channel = bootstrap.bind(0).sync().await().channel();

        LOGGER.info("Server bound on {}", channel.localAddress());
    }

    public int listeningPort() throws UnboundServerException {
        final InetSocketAddress address = ((InetSocketAddress) channel.localAddress());

        if( address == null)
            throw new UnboundServerException();
        else
            return address.getPort();
    }

    /**
     * Allow the specified IP to connect onto the server
     * @param phone
     */
    public void authorize(final Phone phone){
        if(authorizedPhones.add(phone)) {
            LOGGER.info("Authorizing phone {}", phone);

            authorizedPhonesTimeout.schedule(new TimerTask() {
                @Override
                public void run() {
                    LOGGER.info("Removing phone {} from authorized one", phone);
                    authorizedPhones.remove(phone);
                }
            }, TimeUnit.SECONDS.toMillis(10));

        }else
            LOGGER.warn("Tried to authorize an ip which is already allowed");
    }

    /**
     * Try to get the Phone which correspond to the specified IP
     * @param address
     * @return Phone instance if phone is authorized, null otherwise
     */
    public Phone getPhoneIfAuthorized(InetSocketAddress address){
        for (Phone phone : authorizedPhones) {
            if(phone.address().equals(address))
                return phone;
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing server");
        channel.closeFuture().syncUninterruptibly().awaitUninterruptibly();
    }

    public interface HandlerFactory {
        LanSmsHandler newInstance();
    }
}
