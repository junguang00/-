package com.fuicuiedu.idedemo.easyshop_demo.user.register;

import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.User;
import com.fuicuiedu.idedemo.easyshop_demo.model.UserResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class RegisterPersenter extends MvpNullObjectBasePresenter<RegisterView> {

    private Call call;

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void register(final String userName, String password){
        //加载动画
        getView().showProgress();
        call = EasyShopClient.getInstance().register(userName,password);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                //隐藏动画
                getView().hideProgress();
                //显示异常信息
                getView().showMessage(e.getMessage());
                //本地配置清空
                CachePreferences.clearAllData();
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                //拿到返回结果，通过gson转换成实体类
                UserResult userResult = new Gson().fromJson(body,UserResult.class);
                if (userResult.getCode() == 1) {
                    //成功提示
                    getView().showMessage("注册成功！");
                    //将用户信息保存到本地配置
                    User user = userResult.getData();
                    CachePreferences.setUser(user);
                    //调用注册成功方法
                    getView().registerSuccess();
                    // TODO: 2016/11/23 0023 还需要登录环信

                } else if (userResult.getCode() == 2) {
                    //本地配置清空
                    CachePreferences.clearAllData();
                    //隐藏进度条
                    getView().hideProgress();
                    //提示错误信息
                    getView().showMessage(userResult.getMessage());
                    //调用注册失败方法
                    getView().registerFailed();
                }
            }
        });
    }

    // TODO: 2016/11/23 0023 环信模块未加入
}
