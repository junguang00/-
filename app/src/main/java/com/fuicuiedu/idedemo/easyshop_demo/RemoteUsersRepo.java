package com.fuicuiedu.idedemo.easyshop_demo;

import com.feicuiedu.apphx.model.repository.IRemoteUsersRepo;
import com.fuicuiedu.idedemo.easyshop_demo.commons.CurrentUser;
import com.fuicuiedu.idedemo.easyshop_demo.model.GetUsersResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 查找好友
 * 获取好友列表
 */

public class RemoteUsersRepo implements IRemoteUsersRepo{
    @Override
    public List<EaseUser> queryByName(String username) throws Exception {
        Call call = EasyShopClient.getInstance().getSearchUser(username);
        //同步请求
        Response response = call.execute();

        //未成功抛异常
        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }

        String content = response.body().string();
        GetUsersResult result = new Gson().fromJson(content, GetUsersResult.class);
        //通过用户转换类，转换一下
        List<EaseUser> list = CurrentUser.convertAll(result.getDatas());

        return list;
    }

    @Override
    public List<EaseUser> getUsers(List<String> ids) throws Exception {
        /*将好友列表存起来*/
        CurrentUser.setList(ids);
        Call call = EasyShopClient.getInstance().getUsers(ids);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }

        String content = response.body().string();
        GetUsersResult result = new Gson().fromJson(content, GetUsersResult.class);
        if (result.getCode() == 2) {
            throw new Exception(result.getMessage());
        }
        //通过用户转换类，转换一下
        List<EaseUser> list =CurrentUser.convertAll(result.getDatas());

        return list;
    }
}
