package com.polytech.spik.sms.service;

import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.sms.SmsHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mfuntowicz on 13/12/15.
 */
public class LanSmsHandler extends SimpleChannelInboundHandler<SpikMessages.Wrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsHandler.class);

    private SmsHandler handler;

    public LanSmsHandler(SmsHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        handler.onConnected();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        handler.onDisconnected();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SpikMessages.Wrapper msg) throws Exception {
        if(msg.hasContact())
            handler.onContactReceived(msg.getContact());
        else if(msg.hasConversation())
            handler.onConversationReceive(msg.getConversation());
        else if(msg.hasSms())
            handler.onMessage(msg.getSms());
        else if(msg.hasStatusChanged())
            handler.onStatusChanged(msg.getStatusChanged());
        else if(msg.hasSendMessage())
            handler.onSendMessage(msg.getSendMessage());
        else
            LOGGER.warn("Received an unknown message {}", msg);
    }
}
