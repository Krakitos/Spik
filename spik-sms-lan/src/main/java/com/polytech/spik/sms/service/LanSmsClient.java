package com.polytech.spik.sms.service;

import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.sms.SmsServiceClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.compression.SnappyFrameDecoder;
import io.netty.handler.codec.compression.SnappyFrameEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by mfuntowicz on 13/12/15.
 */
public class LanSmsClient extends SmsServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsClient.class);

    private Bootstrap bootstrap;
    private Channel channel;

    public LanSmsClient(LanSmsHandler handler) {
        this(handler, new NioEventLoopGroup(1));
    }

    public LanSmsClient(final LanSmsHandler handler, NioEventLoopGroup loopGroup) {
        bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(loopGroup)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1 << 16, 0, 2, 0, 2));
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new SnappyFrameDecoder());
                        pipeline.addLast(new SnappyFrameEncoder());
                        pipeline.addLast(new ProtobufDecoder(SpikMessages.Wrapper.getDefaultInstance()));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(handler);

                        if(System.getProperty("spik.network.debug", "false").equals("true")) {
                            pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                        }
                    }
                });
    }

    public void connect(String ip, int port) throws InterruptedException {
        LOGGER.info("Trying to connect on {}:{}", ip, port);
        channel = bootstrap.connect(ip, port).await().channel();
    }


    @Override
    public void lowSend(SpikMessages.WrapperOrBuilder msg) {
        if(channel != null){
            LOGGER.debug("Writing message {}", msg);
            channel.writeAndFlush(msg);
        }else {
            LOGGER.warn("Trying to write on an invalid channel");
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing channel");

        if(channel != null)
            channel.close();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
