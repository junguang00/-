package com.fuicuiedu.idedemo.easyshop_demo.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取商品返回对应的实体类
 */
@SuppressWarnings("unused")
public class GoodsResult {

    private int code;
    @SerializedName("msg")
    private String message;
    private List<GoodsInfo> datas;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<GoodsInfo> getData() {
        return datas;
    }
}
