package com.fuicuiedu.idedemo.easyshop_demo.main.me.goodsupload;

import com.fuicuiedu.idedemo.easyshop_demo.commons.MyFileUtils;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsUpLoad;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsUpLoadResult;
import com.fuicuiedu.idedemo.easyshop_demo.model.ImageItem;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopClient;
import com.fuicuiedu.idedemo.easyshop_demo.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Damon on 2016/11/27.
 */

public class GoodsUpLoadPresenter extends MvpNullObjectBasePresenter<GoodsUpLoadView> {

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    /*商品上传*/
    public void upload(GoodsUpLoad goodsLoad, List<ImageItem> list) {
        getView().showProgress();
        call = EasyShopClient.getInstance().upload(goodsLoad, getFiles(list));
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureInUi(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseInUi(okhttp3.Call call, String body) {
                getView().hideProgress();
                GoodsUpLoadResult result = new Gson().fromJson(body, GoodsUpLoadResult.class);
                getView().showMessage(result.getMessage());
                //上传成功
                if (result.getCode() == 1)
                    getView().upLoadSuccess();
            }
        });
    }

    /**
     * 根据ImageItem获取图片文件
     *
     * @param list {@link ImageItem} 的列表
     */
    private ArrayList<File> getFiles(List<ImageItem> list) {
        ArrayList<File> files = new ArrayList<>();
        for (ImageItem imageItem : list) {
            //拿到图片
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }


}
