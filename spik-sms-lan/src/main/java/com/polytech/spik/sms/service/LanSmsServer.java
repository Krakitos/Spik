package com.polytech.spik.sms.service;

import com.polytech.spik.exceptions.UnboundServerException;
import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.sms.SmsHandlerFactory;
import com.polytech.spik.sms.filters.DynamicIpFilter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mfuntowicz on 14/12/15.
 */
public class LanSmsServer implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsServer.class);

    private ServerBootstrap bootstrap;
    private Channel channel;
    private Set<InetAddress> authorizedIps;

    public LanSmsServer(EventLoopGroup eventLoopGroup, final SmsHandlerFactory factory) {
        authorizedIps = new HashSet<>();
        bootstrap = new ServerBootstrap()
                .group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<ServerChannel>() {
                    @Override
                    protected void initChannel(ServerChannel channel) throws Exception {
                        final ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new DynamicIpFilter(authorizedIps));
                        pipeline.addLast(new UniqueIpFilter());
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1 << 16, 0, 2, 0, 2));
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new SnappyFrameDecoder());
                        pipeline.addLast(new SnappyFrameEncoder());
                        pipeline.addLast(new ProtobufDecoder(SpikMessages.Wrapper.getDefaultInstance()));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new LanSmsHandler(factory.newInstance()));

                        if(System.getProperty("spik.network.debug", "false").equals("true")) {
                            pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                        }
                    }
                });
    }

    public void start() throws InterruptedException {
        LOGGER.info("Binding the server");

        channel = bootstrap.bind(10101).sync().await().channel();

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
     * @param address
     */
    public void authorizeIp(InetAddress address){
        if(authorizedIps.add(address))
            LOGGER.info("Authorizing ip {}", address);
        else
            LOGGER.warn("Tried to authorize an ip which is already allowed");
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing server");
        channel.closeFuture().syncUninterruptibly().awaitUninterruptibly();
    }
}
