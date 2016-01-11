package com.holy.jast.example;

import com.holy.jast.JastServer;

/**
 * Created by tiptimes on 16/1/8.
 */
public class Main {

    public static void main(String[] args) {
        //绑定端口 和要扫描的api 包
        JastServer jastServer = new JastServer(8080,"com.holy.jast.example");
        jastServer.start();
    }
}
