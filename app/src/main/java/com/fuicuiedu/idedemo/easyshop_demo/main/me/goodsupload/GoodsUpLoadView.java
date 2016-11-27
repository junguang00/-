package com.fuicuiedu.idedemo.easyshop_demo.main.me.goodsupload;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Damon on 2016/11/27.
 */

public interface GoodsUpLoadView extends MvpView {

    void showProgress();

    void hideProgress();

    /*上传成功*/
    void upLoadSuccess();

    void showMessage(String msg);
}
