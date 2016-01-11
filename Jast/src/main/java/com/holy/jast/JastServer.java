package com.holy.jast;

import com.alibaba.fastjson.JSON;
import com.holy.jast.annotation.Action;
import com.holy.jast.annotation.Api;
import com.holy.jast.http.HttpResponseHelper;
import com.holy.jast.http.RequestParams;
import com.holy.jast.route.RouteEntity;
import com.holy.jast.route.Router;
import com.holy.jast.util.PackageUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by tiptimes on 16/1/8.
 */
public class JastServer {
    private final int port;
    private static final Logger log = LoggerFactory.getLogger("JastServer");
    private static int capacity = 67108864;


    public JastServer(int port, String apiPackageName) {
        this.port = port;
        Jast.getInstance().initApi(apiPackageName);
    }

    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(group).channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast("codec", new HttpServerCodec());
                        ch.pipeline().addLast("aggegator",
                                new HttpObjectAggregator(capacity));
                        ch.pipeline().addLast(new Router());
                    }
                });
        try {
            ChannelFuture f = b.bind().sync();
            log.info(JastServer.class.getName()
                    + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            try{
                group.shutdownGracefully().sync();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }


    }
}

