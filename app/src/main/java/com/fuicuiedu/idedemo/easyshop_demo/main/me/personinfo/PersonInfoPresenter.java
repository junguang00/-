package com.fuicuiedu.idedemo.easyshop_demo.main.me.personinfo;

import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.User;
import com.fuicuiedu.idedemo.easyshop_demo.model.UserResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public class PersonInfoPresenter extends MvpNullObjectBasePresenter<PersonInfoView> {

    private Call call;

    //上传头像
    public void updataAvatar(File file){
        getView().showPrb();
        call = EasyShopClient.getInstance().uploadAvatar(file);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hidePrb();
                UserResult userResult = new Gson().fromJson(body, UserResult.class);
                if (userResult == null) {
                    getView().showMsg("未知错误");
                    return;
                } else if (userResult.getCode() != 1) {
                    getView().showMsg(userResult.getMessage());
                    return;
                }
                User user = userResult.getData();
                CachePreferences.setUser(user);
                getView().updataAvatar(userResult.getData().getHead_Image());
                
                //// TODO: 2016/11/24 0024 环信更新用户头像 
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
}
