package com.polytech.spik.sms.service;

import com.polytech.spik.protocol.SpikMessages;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mfuntowicz on 13/12/15.
 */
public abstract class LanSmsHandler extends SimpleChannelInboundHandler<SpikMessages.Wrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsHandler.class);

    protected LanSmsHandler() {

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        onConnected();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        onDisconnected();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SpikMessages.Wrapper msg) throws Exception {
        if(msg.hasContact())
            onContactReceived(msg.getContact());
        else if(msg.hasConversation())
            onConversationReceive(msg.getConversation());
        else if(msg.hasSms())
            onMessage(msg.getSms());
        else if(msg.hasStatusChanged())
            onStatusChanged(msg.getStatusChanged());
        else if(msg.hasSendMessage())
            onSendMessage(msg.getSendMessage());
        else
            LOGGER.warn("Received an unknown message {}", msg);
    }

    protected abstract void onConnected();

    protected abstract void onDisconnected();

    protected abstract void onContactReceived(SpikMessages.Contact contact);

    protected abstract void onConversationReceive(SpikMessages.Conversation conversation);

    protected abstract void onMessage(SpikMessages.Sms sms);

    protected abstract void onStatusChanged(SpikMessages.StatusChanged statusChanged);

    protected abstract void onSendMessage(SpikMessages.SendMessage sendMessage);
}
