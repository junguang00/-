package com.fuicuiedu.idedemo.easyshop_demo.network;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class EasyShopApi {
    //服务器路径
    static String BASE_URL = "http://wx.feicuiedu.com:9094/yitao/";
    /*图片的基路径*/
    public static String IMAGE_URL = "http://wx.feicuiedu.com:9094/";

    //注册借口
    static String REGISTER = "UserWeb?method=register";

    //登录接口
    static String LOGIN = "UserWeb?method=login";

    //更新接口(更新昵称，用户头像)
    static String UPDATA = "UserWeb?method=update";

    //获取商品
    static final String GETGOODS = "GoodsServlet?method=getAll";

    //获取商品详情
    static final String DETAIL = "GoodsServlet?method=view";

    //删除商品
    static final String DELETE = "GoodsServlet?method=delete";



}
