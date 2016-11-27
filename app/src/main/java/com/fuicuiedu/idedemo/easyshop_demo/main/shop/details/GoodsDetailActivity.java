package com.fuicuiedu.idedemo.easyshop_demo.main.shop.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.components.AvatarLoadOptions;
import com.fuicuiedu.idedemo.easyshop_demo.components.ProgressDialogFragment;
import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsDetail;
import com.fuicuiedu.idedemo.easyshop_demo.model.User;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopApi;
import com.fuicuiedu.idedemo.easyshop_demo.user.login.LoginActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends MvpActivity<GoodsDetailVIew,GoodsDetailPresenter> implements GoodsDetailVIew {

    private static final String UUID = "uuid";
    /*从不同页面进入详情页面的状态值,0为默认值,1是我的商品页面进入*/
    private static final String STATE = "state";

    public static Intent getStartIntent(Context context, String uuid, int state) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(UUID, uuid);
        intent.putExtra(STATE, state);
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    /*使用开源库CircleIndicator来实现ViewPager的圆点指示器。*/
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_detail_name)
    TextView tv_detail_name;
    @BindView(R.id.tv_detail_price)
    TextView tv_detail_price;
    @BindView(R.id.tv_detail_master)
    TextView tv_detail_master;
    @BindView(R.id.tv_detail_describe)
    TextView tv_detail_describe;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.tv_goods_error)
    TextView tv_goods_error;
    @BindView(R.id.btn_detail_message)
    Button btn_detail_message;

    private String str_uuid;
    private ArrayList<ImageView> list;
    /*用来存放图片uri的list*/
    private ArrayList<String> list_uri;
    private GoodsDetailAdapter adapter;
    private ProgressDialogFragment dialogFragment;
    private ActivityUtils activityUtils;
    private User goods_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        activityUtils = new ActivityUtils(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adapter = new GoodsDetailAdapter(list);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        viewPager.setAdapter(adapter);
        /*ViewPager的Item单击事件*/
        adapter.setListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //点击图片，跳转到图片展示的avitivty
                Intent intent = GoodsDetailInfoActivity.getStartIntent(GoodsDetailActivity.this, list_uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }

    /*对页面数据进行初始化*/
    private void init() {
        /*商品在商品表中的主键*/
        str_uuid = getIntent().getStringExtra(UUID);
        /*从不同页面进入详情页面的状态值,0为默认值,1是我的商品页面进入*/
        int btn_show = getIntent().getIntExtra(STATE, 0);
        //来自我的商品页面
        if (btn_show == 1) {
            /*我的商品页面进入显示删除按钮,隐藏下方联系按钮*/
            tv_goods_delete.setVisibility(View.VISIBLE);
            btn_detail_message.setVisibility(View.GONE);
        }
        presenter.getData(str_uuid);
    }

    /*将图片路径转化为一个ImageView*/
    private void getImage(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance()
                    .displayImage(EasyShopApi.IMAGE_URL + list.get(i),
                            imageView, AvatarLoadOptions.build_item());
            this.list.add(imageView);
        }
    }

    //点发消息，或者删除商品
    @OnClick({R.id.btn_detail_message, R.id.tv_goods_delete})
    public void onClick(View view) {
        //判断登录状态
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.btn_detail_message:
                //根据环信ID判断商品归属，自己不能给自己发消息
                if (goods_user.getHx_Id().equals(CachePreferences.getUser().getHx_Id())) {
                    activityUtils.showToast("这个商品是自己发布的哦！");
                    return;
                }
                // TODO: 2016/11/27 跳转到发送消息页面，环信实现
                break;
            case R.id.tv_goods_delete:
                //弹一个警告，是否删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.goods_title_delete);
                builder.setMessage(R.string.goods_info_delete);
                //设置确认按钮，点击删除
                builder.setPositiveButton(R.string.goods_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //执行删除方法
                        presenter.delete(str_uuid);
                    }
                });
                builder.setNegativeButton(R.string.popu_cancle, null);
                builder.create().show();
                break;
        }
    }

    @Override
    public void showProgress() {
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
        }
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        dialogFragment.dismiss();
    }

    @Override
    public void setImageData(ArrayList<String> viewList) {
        list_uri = viewList;
        //加载图片
        getImage(viewList);
        adapter.notifyDataSetChanged();
        //确认图片数量后，创建圆点指示器
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setData(GoodsDetail data, User goods_user) {
        //设置数据展示
        this.goods_user = goods_user;
        tv_detail_name.setText(data.getName());
        tv_detail_price.setText(getString(R.string.goods_money, data.getPrice()));
        tv_detail_master.setText(getString(R.string.goods_detail_master, goods_user.getNick_Name()));
        tv_detail_describe.setText(data.getDescription());
    }

    @Override
    public void showError() {
        tv_goods_error.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.goods_overdue);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void deleteEnd() {
        finish();
    }
}
