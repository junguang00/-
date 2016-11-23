package com.fuicuiedu.idedemo.easyshop_demo.user.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public interface LoginView extends MvpView {

    void showProgress();

    void hideProgress();

    /*登录失败*/
    void loginFailed();

    /*登录成功*/
    void loginSussed();

    /*返回结果信息*/
    void showMessage(String msg);
}
