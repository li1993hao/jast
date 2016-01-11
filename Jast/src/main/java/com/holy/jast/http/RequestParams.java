package com.holy.jast.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by tiptimes on 16/1/8.
 */
public class RequestParams {
    private Map<String, List<String>> getParams;
    private Map<String, String> postParams;
    private List<FileUpload> fileUploads;
    private HttpHeaders httpHeaders;
    private String path;

    public RequestParams(String path, HttpHeaders httpHeaders, Map<String, List<String>> getParams, Map<String, String> postParams, List<FileUpload> fileUpload){
        this.path = path;
        this.httpHeaders = httpHeaders;
        this.getParams = getParams;
        this.postParams = postParams;
        this.fileUploads = fileUpload;
    }

    public FileUpload fileUpload(String name){
        for (FileUpload fileUpload: fileUploads){
            if(fileUpload.getName().equals(name)){
                return fileUpload;
            }
        }

        return  null;
    }

    public String header(String key){
        return  httpHeaders.get(key);
    }

    public String get(String key){
        return  getParams.get(key).get(0);
    }

    public List<String> getArray(String key){
        return getParams.get(key);
    }

    public String post(String key){
        return  postParams.get(key);
    }

    public String request(String key){
        String getResult = get(key);

        if(getResult != null){
            return  getResult;
        }

        return  post(key);
    }


    public Map<String, List<String>> getGetParams() {
        return getParams;
    }

    public Map<String, String> getPostParams() {
        return postParams;
    }

    public List<FileUpload> getFileUploads() {
        return fileUploads;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public String getPath() {
        return path;
    }
}
