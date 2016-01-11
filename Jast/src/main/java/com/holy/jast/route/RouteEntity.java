package com.holy.jast.route;

import java.lang.reflect.Method;

/**
 * Created by tiptimes on 16/1/8.
 */
public class RouteEntity{
    private Object api;
    private Method action;

    public RouteEntity(Object api, Method action){
        this.api = api;
        this.action = action;
    }

    public RouteEntity(){

    }

    public Object getApi() {
        return api;
    }

    public void setApi(Object api) {
        this.api = api;
    }

    public Method getAction() {
        return action;
    }

    public void setAction(Method action) {
        this.action = action;
    }
}
