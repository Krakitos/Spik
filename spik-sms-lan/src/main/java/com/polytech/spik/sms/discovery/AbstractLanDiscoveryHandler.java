package com.polytech.spik.sms.discovery;

import com.polytech.spik.protocol.DiscoveryMessages;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

/**
 * Created by mfuntowicz on 24/10/15.
 */
public class AbstractLanDiscoveryHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        final DiscoveryMessages.DiscoveryMessage msg =
                DiscoveryMessages.DiscoveryMessage.parseFrom(new ByteBufInputStream(packet.content()));

        if(msg.hasRequest()){
            handleDiscoveryRequest(ctx, packet.sender(), msg.getRequest());
        }else {
            handleDiscoveryResponse(ctx, packet.sender(), msg.getResponse());
        }
    }

    protected void handleDiscoveryRequest(ChannelHandlerContext ctx,
                                          InetSocketAddress sender,
                                          DiscoveryMessages.DiscoveryRequest request) {
    }

    protected void handleDiscoveryResponse(ChannelHandlerContext ctx,
                                           InetSocketAddress sender,
                                           DiscoveryMessages.DiscoveryResponse response) {
    }
}
