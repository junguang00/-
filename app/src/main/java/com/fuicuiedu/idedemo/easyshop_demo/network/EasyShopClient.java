package com.fuicuiedu.idedemo.easyshop_demo.network;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class EasyShopClient {

    private static EasyShopClient easyShopClient;
    private OkHttpClient okHttpClient;

    private EasyShopClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new BombInterceptor())
                .build();
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

    /**
     * 注册
     * <p>
     * post
     *
     * @param UserName 用户名
     * @param Password 秘密
     */
    public Call register_demo(String UserName, String Password){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", UserName);
            jsonObject.put("password", Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(null, jsonObject.toString());

        Request request = new Request.Builder()
                .url(EasyShopApi_Demo.REGISTER)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 登录
     * <p>
     * GET
     *
     * // URL编码参数
     "username"=username
     "password"=password
     */
    public Call login_demo(String username, String password){

        Request request = new Request.Builder()
                .url(EasyShopApi_Demo.LOGIN +
                        "?username=" + username +
                        "&password=" + password
                    )
                .get()
                .build();
        return okHttpClient.newCall(request);
    }


}
