package com.polytech.spik.services.sms;

import com.intellij.openapi.util.SystemInfo;
import com.polytech.spik.domain.Phone;
import com.polytech.spik.exceptions.UnboundServerException;
import com.polytech.spik.protocol.DiscoveryMessages;
import com.polytech.spik.sms.SmsHandlerFactory;
import com.polytech.spik.sms.discovery.AbstractLanDiscoveryHandler;
import com.polytech.spik.sms.discovery.LanDiscoveryServer;
import com.polytech.spik.sms.service.LanSmsServer;
import com.polytech.util.ComputerInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by momo- on 15/12/2015.
 */
public class LanSmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsService.class);

    private EventLoopGroup eventLoopGroup;
    private LanSmsServer lanSmsServer;
    private LanDiscoveryServer lanDiscoveryServer;

    public LanSmsService(SmsHandlerFactory handlerFactory) {
        eventLoopGroup = new NioEventLoopGroup(2, Executors.newFixedThreadPool(2));
        lanSmsServer = new LanSmsServer(eventLoopGroup, handlerFactory);
        lanDiscoveryServer = new LanDiscoveryServer(eventLoopGroup, new AbstractLanDiscoveryHandler(){
            @Override
            protected void handleDiscoveryRequest(ChannelHandlerContext ctx, InetSocketAddress sender, DiscoveryMessages.DiscoveryRequest request) {
                final Phone phone = new Phone(
                        request.getName(),
                        request.getManufacturer(),
                        request.getModel(),
                        request.getOs().name(),
                        request.getSdkVersion(),
                        sender.getHostString(),
                        sender.getPort()
                );

                LOGGER.info("Received discovery request from {}", phone);

                try {
                    final int port = lanSmsServer.listeningPort();
                    final DiscoveryMessages.DiscoveryMessage response = createDiscoveryResponse(port);
                    lanSmsServer.authorizeIp(sender.getAddress());

                    LOGGER.info("Sending discovery response to {}", sender);

                    ctx.writeAndFlush(response);

                }catch (UnboundServerException e){
                    LOGGER.warn("Unable to response to the discovery request {}", e.getMessage());
                }
            }

            private DiscoveryMessages.DiscoveryMessage createDiscoveryResponse(int port){
                DiscoveryMessages.DiscoveryResponse response = DiscoveryMessages.DiscoveryResponse.newBuilder()
                        .setName(ComputerInfo.hostname())
                        .setOs(SystemInfo.isWindows ? DiscoveryMessages.OperatingSystem.WINDOWS : SystemInfo.isMac ? DiscoveryMessages.OperatingSystem.MACOS : DiscoveryMessages.OperatingSystem.LINUX)
                        .setVersion(SystemInfo.OS_VERSION)
                        .setPort(port)
                        .build();

                return DiscoveryMessages.DiscoveryMessage.newBuilder()
                        .setResponse(response)
                        .build();
            }
        });
    }

    public void run() throws InterruptedException {
        lanSmsServer.start();
    }

    public void stop() throws IOException {
        lanSmsServer.close();
    }
}
