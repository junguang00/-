package com.fuicuiedu.idedemo.easyshop_demo.main.me.personinfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/11/23 0023.
 */

public interface PersonInfoView extends MvpView {

    void showPrb();

    void hidePrb();

    void showMsg(String msg);

    void updataAvatar(String url);
}
