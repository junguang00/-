package com.fuicuiedu.idedemo.easyshop_demo.user.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public interface RegisterView extends MvpView {

    void showProgress();

    void hideProgress();

    /*注册失败*/
    void registerFailed();

    /*注册成功*/
    void registerSuccess();

    /*返回结果信息*/
    void showMessage(String msg);

    /*用户名和密码不符合要求*/
    void showUserPasswordError(String msg);
}
