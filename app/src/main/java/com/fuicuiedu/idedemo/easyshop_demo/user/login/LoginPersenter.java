package com.fuicuiedu.idedemo.easyshop_demo.user.login;

import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.User;
import com.fuicuiedu.idedemo.easyshop_demo.model.UserResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class LoginPersenter extends MvpNullObjectBasePresenter<LoginView> {

    private Call call;

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void login(final String userName, String password){
        //加载动画
        getView().showProgress();
        call = EasyShopClient.getInstance().login(userName,password);
        call.enqueue(new UICallback() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                //隐藏动画
                getView().hideProgress();
                //显示异常信息
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                //拿到返回结果，通过gson转换成实体类
                UserResult userResult = new Gson().fromJson(body,UserResult.class);
                if (userResult.getCode() == 1) {
                    //成功提示
                    getView().showMessage("登录成功");
                    //将用户信息保存到本地配置
                    User user = userResult.getData();
                    CachePreferences.setUser(user);
                    //调用登录成功方法
                    getView().loginSussed();
                    // TODO: 2016/11/23 0023 还需要登录环信

                } else if (userResult.getCode() == 2) {
                    //隐藏进度条
                    getView().hideProgress();
                    //提示错误信息
                    getView().showMessage(userResult.getMessage());
                    //调用登录失败方法
                    getView().loginFailed();
                }
            }
        });
    }

    // TODO: 2016/11/23 0023 环信模块未加入
}
