package com.polytech.spik.sms.service;

import com.polytech.spik.exceptions.UnboundChannelException;
import com.polytech.spik.protocol.SpikMessages;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mfuntowicz on 13/12/15.
 */
public abstract class LanSmsHandler extends SimpleChannelInboundHandler<SpikMessages.Wrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanSmsHandler.class);

    private Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        channel = ctx.channel();
        onConnected();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        channel = null;
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

    public void write(SpikMessages.Wrapper msg) throws UnboundChannelException {
        if(channel != null){
            LOGGER.trace("Writing message to {}", channel.remoteAddress());

            channel.writeAndFlush(msg);
        }else{
            LOGGER.warn("Attempt to write message to a null channel");

            throw new UnboundChannelException();
        }
    }

    protected abstract void onConnected();

    protected abstract void onDisconnected();

    protected abstract void onContactReceived(SpikMessages.Contact contact);

    protected abstract void onConversationReceive(SpikMessages.Conversation conversation);

    protected abstract void onMessage(SpikMessages.Sms sms);

    protected abstract void onStatusChanged(SpikMessages.StatusChanged statusChanged);

    protected abstract void onSendMessage(SpikMessages.SendMessage sendMessage);
}
