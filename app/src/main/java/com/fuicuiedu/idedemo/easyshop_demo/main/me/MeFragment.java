package com.fuicuiedu.idedemo.easyshop_demo.main.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.user.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    @BindView(R.id.iv_user_head)
    ImageView iv_user_head;
    @BindView(R.id.tv_login)
    TextView tv_login;

    private View view;
    private ActivityUtils activityUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO: 2016/11/16 判断用户是否登录，更改用户头像并且显示用户名 
    }

    @OnClick({R.id.iv_user_head, R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick() {
        // TODO: 2016/11/16 判断用户是否登录，来确定跳转位置
        activityUtils.startActivity(LoginActivity.class);
    }
}
