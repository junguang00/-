package com.fuicuiedu.idedemo.easyshop_demo.main.shop.details;

import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsDetail;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsDetailResult;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Damon on 2016/11/27.
 */

public class GoodsDetailPresenter extends MvpNullObjectBasePresenter<GoodsDetailView> {

    //获取详情的call
    private Call getDetailCall;
    //删除商品的call
    private Call deleteCall;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (getDetailCall != null) getDetailCall.cancel();
        if (deleteCall != null) deleteCall.cancel();
    }

    /*获取商品的详细数据*/
    public void getData(String uuid) {
        getView().showProgress();
        getDetailCall = EasyShopClient.getInstance().getGoodsData(uuid);
        getDetailCall.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.toString());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult goodsDetailResult = new Gson().
                        fromJson(body, GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1) {
                    GoodsDetail goodsDetail = goodsDetailResult.getDatas();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < goodsDetail.getPages().size(); i++) {
                        list.add(goodsDetail.getPages().get(i).getUri());
                    }
                    getView().setImageData(list);
                    getView().setData(goodsDetail, goodsDetailResult.getUser());
                } else {
                    getView().showError();
                }
            }
        });
    }

    /*删除商品*/
    public void delete(String uuid) {
        deleteCall = EasyShopClient.getInstance().deleteGoods(uuid);
        deleteCall.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.toString());
            }

            @Override
            public void onResponseInUi(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult goodsDetailResult = new Gson().
                        fromJson(body, GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1) {
                    //执行删除删除商品方法
                    getView().deleteEnd();
                    getView().showMessage("删除成功");
                }
            }
        });
    }

}
