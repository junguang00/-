package com.fuicuiedu.idedemo.easyshop_demo.main.shop;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView> {

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //刷新数据
    public void refreshData(String type){

    }
    //加载更多
    public void loadData(String type){

    }
}
