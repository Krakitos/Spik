package com.polytech.spik.sms.filters;

import com.polytech.spik.domain.Phone;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Created by mfuntowicz on 25/10/15.
 */
public class DynamicIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicIpFilter.class);

    private final Set<Phone> authorizedPhones;

    public DynamicIpFilter(final Set<Phone> authorizedPhones) {
        this.authorizedPhones = authorizedPhones;
    }

    @Override
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress address) throws Exception {
        for (Phone phone : authorizedPhones) {
            if(phone.address().getAddress().equals(address.getAddress()))
                return true;

        }
        return false;
    }

    @Override
    protected void channelAccepted(ChannelHandlerContext ctx, InetSocketAddress address) {
        LOGGER.trace("Accepted connection from {}", address.getAddress());
        super.channelAccepted(ctx, address);
    }

    @Override
    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress address) {
        LOGGER.trace("Rejected connection from {}", address.getAddress());
        return super.channelRejected(ctx, address);
    }

    /**
     * Remove all authorized IPs, and start deny all connection attempt
     */
    public void clear(){
        LOGGER.trace("Removing all allowed IPs");
        authorizedPhones.clear();
    }
}
