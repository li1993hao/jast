package com.holy.jast.route;

import com.holy.jast.Jast;
import com.holy.jast.util.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * Created by tiptimes on 16/1/8.
 */
public class Router extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        HttpRequest request = null;
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            try{
                Jast.getInstance().handle(ctx, content, request);
            }finally {
                content.release();
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.log.error("something error", cause);
        ctx.close();
    }
}
