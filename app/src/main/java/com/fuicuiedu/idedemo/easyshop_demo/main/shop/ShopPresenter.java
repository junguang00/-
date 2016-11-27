package com.fuicuiedu.idedemo.easyshop_demo.main.shop;

import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView> {

    private Call call;
    private int pageInt = 1;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //刷新数据
    public void refreshData(String type){
        getView().showRefresh();
        //永远是最新数据，所以页面写死为1
        call = EasyShopClient.getInstance().getGoods(1,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    //成功
                    case 1:
                        //还没有商品
                        if (goodsResult.getData().size() == 0){
                            getView().addRefreshData(goodsResult.getData());
                            getView().showRefreshEnd();
                        }else{
                            getView().addRefreshData(goodsResult.getData());
                            getView().showRefreshEnd();
                        }
                        //分页改为2，之后就要加载更多了
                        pageInt = 2;
                        break;
                    //失败或者其他
                    default:
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }


    //加载数据
    public void loadData(String type){
        getView().showLoadMoreLoading();
        call = EasyShopClient.getInstance().getGoods(pageInt,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getData().size() == 0){
                            getView().showLoadMoreEnd();
                        }
                        else{
                            getView().addMoreData(goodsResult.getData());
                            getView().hideLoadMore();
                        }
                        //分页加载
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }
}
