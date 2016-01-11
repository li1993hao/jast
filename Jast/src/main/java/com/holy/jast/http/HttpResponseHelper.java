package com.holy.jast.http;

import com.holy.jast.util.Date;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by tiptimes on 16/1/8.
 */
public class HttpResponseHelper {
    private ChannelHandlerContext ctx;
    private HttpRequest httpRequest;

    public HttpResponseHelper(ChannelHandlerContext ctx,HttpRequest httpRequest){
        this.ctx = ctx;
        this.httpRequest = httpRequest;
    }

    public  void respond(byte[] content){
        send(HttpResponseStatus.OK,content);
    }

    public  void respond(byte[] content,String contentType){
        if(contentType == null){
            send(HttpResponseStatus.OK,content);
        }else{
            send(HttpResponseStatus.OK,content,contentType);
        }
    }


    public void respondStatus(String contentStr,HttpResponseStatus status) {
        send(status,contentStr.getBytes(),"text/plain");
    }

    public void respondStatus(HttpResponseStatus status) {
        send(status);
    }

    private void send(HttpResponseStatus status) {
        send(status,new byte[]{}, new HashMap<String, Object>());
    }

    private void send( HttpResponseStatus status,byte[] content) {
        send(status, content, new HashMap<String, Object>());
    }

    private void send(HttpResponseStatus status,byte[] content,String contentType) {
        send(status,content, new HashMap<String, Object>());
    }


    private  void send(HttpResponseStatus status,byte[] content,Map<String,Object> head) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.wrappedBuffer(content));

        if(head != null){
            for(String key : head.keySet()){
                response.headers().set(key, head.get(key));
            }
        }
        response.headers().set(HttpHeaders.Names.SERVER, "JastServer");
        response.headers().set("X-Powered-By", "Jast");
        response.headers().set(HttpHeaders.Names.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING,HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.CONTENT_ENCODING,HttpHeaders.Values.GZIP); //开启gzip压缩
        response.headers().set("X-Content-Type-Options", "nosniff");
        response.headers().set(HttpHeaders.Names.DATE, Date.httpDate());

        response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());

        ctx.writeAndFlush(response);
    }
}
