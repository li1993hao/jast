package com.holy.jast;
import com.alibaba.fastjson.JSON;
import com.holy.jast.annotation.Action;
import com.holy.jast.annotation.Api;
import com.holy.jast.http.RequestParams;
import com.holy.jast.http.HttpResponseHelper;
import com.holy.jast.route.RouteEntity;
import com.holy.jast.util.GZip;
import com.holy.jast.util.PackageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by tiptimes on 16/1/8.
 */
public class Jast {
    private static Jast jast = new Jast();
    private static Map<String,RouteEntity> routeEntityMap = new HashMap<String,RouteEntity>();
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk

    private Jast(){
    }

    /**
     * 初始化api组件
     * @param pk
     */
    public void initApi(String pk){
        Set<Class<?>> classes = PackageUtil.getClasses(pk, Api.class);
        for(Class cls:classes){
            //获取默认路由前缀
            Api api = (Api)cls.getAnnotation(Api.class);
            String preRoute = api.route();

            //建立路由到action 的映射关系
            Method[]  methods =  cls.getDeclaredMethods();
            for(Method method:methods){
                Action action = method.getAnnotation(Action.class);
                if(action != null){
                    RouteEntity routeEntity = new RouteEntity();
                    String[] routes = action.route();
                    for(String route:routes){
                        String routeKey = preRoute + route;
                        if(routeEntityMap.containsKey(routeKey)){
                            System.out.println(routeKey + " 路由重复定义!");
                        }else{
                            routeEntity.setAction(method);
                            try{
                                routeEntity.setApi(cls.newInstance());
                                routeEntityMap.put(routeKey, routeEntity);
                            }catch (Exception e){
                                System.out.print("controller 必须要有无参构造器！");
                            }
                        }
                    }
                }
            }
        }
    }



    public static Jast getInstance(){
        return jast;
    }

    /**
     * 初始化 请求参数
     * @param httpRequest
     * @param msg
     * @return
     */
    private RequestParams initHttpParams(HttpRequest httpRequest, HttpContent msg){
        QueryStringDecoder decoderQuery = new QueryStringDecoder(httpRequest.getUri());

        String path = decoderQuery.path();
        HttpHeaders httpHeaders = httpRequest.headers();
        Map<String, List<String>> getParams = decoderQuery.parameters();
        Map<String, String> postParams = new HashMap<String, String>();
        List<FileUpload> fileUploads = new ArrayList<FileUpload>();

        if(httpRequest.getMethod().equals(HttpMethod.POST)){
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, httpRequest);
            List<InterfaceHttpData> ld =decoder.getBodyHttpDatas();
            for(InterfaceHttpData data:ld){
                InterfaceHttpData.HttpDataType dataType = data.getHttpDataType();
                if(dataType.equals(InterfaceHttpData.HttpDataType.Attribute)){
                    Attribute attribute =  (Attribute)data;
                    try{
                        postParams.put(attribute.getName(), attribute.getValue());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(dataType.equals(InterfaceHttpData.HttpDataType.FileUpload)){
                    FileUpload fileUpload = (FileUpload)data;
                    fileUploads.add(fileUpload);
                }
            }
        }

        return new RequestParams(path,httpHeaders, getParams, postParams, fileUploads);
    }

    /**
     * 请求单一入口
     * @param ctx
     * @param msg
     * @param httpRequest
     */
    public void handle(ChannelHandlerContext ctx,HttpContent msg,HttpRequest httpRequest){

        RequestParams httpParams = initHttpParams(httpRequest, msg);

        String routeKey = httpParams.getPath();
        RouteEntity routeEntity = routeEntityMap.get(routeKey);
        HttpResponseHelper httpResponseHelper = new HttpResponseHelper(ctx, httpRequest);
        if(routeEntity == null){
            httpResponseHelper.respondStatus(HttpResponseStatus.NOT_FOUND);
        }else{
            Object apiObject = routeEntity.getApi();
            try {
                Method clsMethod = routeEntity.getAction();
                Object o = clsMethod.invoke(apiObject,httpParams);
                String str = JSON.toJSONString(o);

                httpResponseHelper.respond(GZip.compressToByte(str), "application/json; charset=utf-8");
            }catch (Exception e){
                httpResponseHelper.respondStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }
    }
}

