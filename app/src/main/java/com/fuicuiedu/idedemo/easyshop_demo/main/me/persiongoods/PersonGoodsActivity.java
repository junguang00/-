package com.fuicuiedu.idedemo.easyshop_demo.main.me.persiongoods;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.main.shop.ShopAdapter;
import com.fuicuiedu.idedemo.easyshop_demo.main.shop.ShopView;
import com.fuicuiedu.idedemo.easyshop_demo.main.shop.details.GoodsDetailActivity;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PersonGoodsActivity extends MvpActivity<ShopView,PersonGoodsPresenter> implements ShopView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*下拉刷新和上拉加载的控件*/
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_load_error)
    TextView tv_load_error;
    @BindView(R.id.tv_load_empty)
    TextView tv_load_empty;

    @BindString(R.string.load_more_end)
    String load_more_end;

    /**
     * 获取商品时,商品的类型,获取全部商品时为空
     */
    private String pageType = "";

    private ActivityUtils activityUtils;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        shopAdapter = new ShopAdapter();
        setContentView(R.layout.activity_person_goods);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*设置ToolBar的监听事件*/
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        initView();
        recyclerView.setAdapter(shopAdapter);

        //点击跳转到详情页
        shopAdapter.setListener(new ShopAdapter.onItemClickedListener() {
            @Override
            public void onPhotoClicked(GoodsInfo goodsInfo) {
                //跳转到详情页，来自我的商品页面
                Intent intent = GoodsDetailActivity.getStartIntent(PersonGoodsActivity.this, goodsInfo.getUuid(), 1);
                startActivity(intent);
            }
        });
    }

    /*RecyclerView和PtrClassicFrameLayout的初始化*/
    private void initView() {
        /*设置recyclerView的manager*/
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        /*设置refreshLayout*/
        /*使用本对象作为key，来记录上一次刷新时间，如果两次下拉间隔太近，不会触发刷新方法*/
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setBackgroundResource(R.color.recycler_bg);
        /*关闭Header所耗时长*/
        ptrFrameLayout.setDurationToCloseHeader(1500);

        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*ToolBar设置菜单选项*/
        getMenuInflater().inflate(R.menu.menu_goods_type, menu);
        return true;
    }

    /**
     * ToolBar菜单对应的单击事件
     * 注：这里商品的种类为自定义
     */
    private final Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };

    //每次进入这个页面,当前页面没有数据时,刷新
    @Override
    protected void onStart() {
        super.onStart();
        if (shopAdapter.getItemCount() == 0) {
                    ptrFrameLayout.autoRefresh();
        }
    }

    @NonNull
    @Override
    public PersonGoodsPresenter createPresenter() {
        return new PersonGoodsPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRefresh() {
        tv_load_error.setVisibility(View.INVISIBLE);
        tv_load_empty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRefreshError(String msg) {
        ptrFrameLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        ptrFrameLayout.refreshComplete();
        tv_load_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefresh() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tv_load_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        ptrFrameLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(load_more_end);
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        shopAdapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        shopAdapter.clear();
        if (data != null) shopAdapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
