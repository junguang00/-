package com.fuicuiedu.idedemo.easyshop_demo.main.me.personinfo;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class PersonInfoPresenter extends MvpNullObjectBasePresenter<PersonInfoView> {

    private Call call;

    //上传头像
    public void updataAvatar(){

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
}
