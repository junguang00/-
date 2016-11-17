package com.fuicuiedu.idedemo.easyshop_demo.network;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class EasyShopClient {

    private static EasyShopClient easyShopClient;
    private OkHttpClient okHttpClient;

    private EasyShopClient() {
        okHttpClient = new OkHttpClient();
    }

    public static EasyShopClient getInstance() {
        if (easyShopClient == null) {
            easyShopClient = new EasyShopClient();
        }
        return easyShopClient;
    }

    /**
     * 注册
     * <p>
     * post
     *
     * @param username 用户名
     * @param password 秘密
     */
    public Call register(String username, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.REGISTER)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }


}
