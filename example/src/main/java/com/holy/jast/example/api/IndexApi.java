package com.holy.jast.example.api;

import com.holy.jast.annotation.Action;
import com.holy.jast.annotation.Api;
import com.holy.jast.example.vo.User;
import com.holy.jast.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiptimes on 16/1/8.
 */
@Api
public class IndexApi {

    @Action(route = {"", "index"})
    public String index(RequestParams requestParams){
        return  "hello jast!";
    }

    @Action(route = {"users"})
    public List<User> getUserList(RequestParams requestParams){

        List<User> list = new ArrayList<User>();
        for(int i=0; i<10; i++){
            User user = new User();
            user.setName(requestParams.request("name"));
            user.setAddress(requestParams.request("address"));
            user.setAge(10 + i);
            list.add(user);
        }

        return  list;
    }
}
