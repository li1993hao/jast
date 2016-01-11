package com.holy.jast.example.api;

import com.holy.jast.annotation.Action;
import com.holy.jast.annotation.Api;
import com.holy.jast.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiptimes on 16/1/8.
 */
@Api(route = "/user/")
public class UserApi {

    @Action(route = {"users"})
    public List<com.holy.jast.example.vo.User> getUserList(RequestParams requestParams){

        List<com.holy.jast.example.vo.User> list = new ArrayList<com.holy.jast.example.vo.User>();
        for(int i=0; i<10; i++){
            com.holy.jast.example.vo.User user = new com.holy.jast.example.vo.User();
            user.setName("jast" + i);
            user.setAddress("beijing China");
            user.setAge(10 + i);
            list.add(user);
        }

        return  list;
    }
}
