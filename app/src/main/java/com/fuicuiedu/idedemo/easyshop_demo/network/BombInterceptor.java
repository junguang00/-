package com.fuicuiedu.idedemo.easyshop_demo.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 处理Bomb需要的统一的头字段
 * 作者：yuanchao on 2016/8/16 0016 12:03
 * 邮箱：yuanchao@feicuiedu.com
 */
public class BombInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        // 用于让bomb服务器，区分是哪一个应用
        builder.addHeader("X-Bmob-Application-Id", "623aaef127882aed89b9faa348451da3");
        // 用于授权
        builder.addHeader("X-Bmob-REST-API-Key", "c00104962a9b67916e8cbcb9157255de");
        // 请求和响应都统一使用json格式
        builder.addHeader("Content-Type","application/json");

        return chain.proceed(builder.build());
    }
}
