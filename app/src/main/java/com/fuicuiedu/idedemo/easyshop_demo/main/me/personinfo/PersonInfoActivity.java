package com.fuicuiedu.idedemo.easyshop_demo.main.me.personinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.components.AvatarLoadOptions;
import com.fuicuiedu.idedemo.easyshop_demo.components.PicWindow;
import com.fuicuiedu.idedemo.easyshop_demo.components.ProgressDialogFragment;
import com.fuicuiedu.idedemo.easyshop_demo.main.MainActivity;
import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.ItemShow;
import com.fuicuiedu.idedemo.easyshop_demo.model.User;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopApi;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fuicuiedu.idedemo.easyshop_demo.R.id.iv_user_head;

public class PersonInfoActivity extends MvpActivity<PersonInfoView, PersonInfoPresenter> implements PersonInfoView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.listView)
    ListView listView;

    private ActivityUtils activityUtils;
    private ProgressDialogFragment progressDialogFragment;
    private PersonInfoAdapter adapter;
    private List<ItemShow> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions,ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PersonInfoAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        //获取头像
        updataAvatar(CachePreferences.getUser().getHead_Image());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }

    /*从CachePreferences获取用户数据*/
    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow(getResources().getString(R.string.username), user.getName()));
        list.add(new ItemShow(getResources().getString(R.string.hx_id), user.getNick_Name()));
        list.add(new ItemShow(getResources().getString(R.string.nickname), user.getHx_Id()));
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    activityUtils.showToast(getResources().getString(R.string.username_update));
                    break;
                case 1:
                    activityUtils.startActivity(NickNameActivity.class);
                    break;
                case 2:
                    activityUtils.showToast(getResources().getString(R.string.id_update));
                    break;
            }
        }
    };

    @NonNull
    @Override
    public PersonInfoPresenter createPresenter() {
        return new PersonInfoPresenter();
    }

    @Override
    public void showPrb() {
        if (progressDialogFragment == null) {
            progressDialogFragment = new ProgressDialogFragment();
        }
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");

    }

    @Override
    public void hidePrb() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);

    }


    @Override
    public void updataAvatar(String url) {
        /*设置头像*/
        // TODO: 2016/11/23 0023 设置头像待实现
    }

    @OnClick({R.id.btn_login_out, iv_user_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case iv_user_head:
                // TODO: 2016/11/23 0023 待实现
                activityUtils.showToast("未实现");
                break;
            case R.id.btn_login_out:
                // TODO: 2016/11/23 0023 环信登出操作
                CachePreferences.clearAllData();
                Intent intent = new Intent(PersonInfoActivity.this, MainActivity.class);
                /*清除所有旧的Activity*/
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
