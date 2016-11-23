package com.fuicuiedu.idedemo.easyshop_demo.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.commons.LogUtils;
import com.fuicuiedu.idedemo.easyshop_demo.main.NoLoginFragment;
import com.fuicuiedu.idedemo.easyshop_demo.main.me.MeFragment;
import com.fuicuiedu.idedemo.easyshop_demo.main.shop.ShopFragment;
import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 易淘首页面
 */
public class MainActivity extends AppCompatActivity {
    @BindViews({R.id.tv_shop,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    TextView[] textViews;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_title)
    TextView tv_title;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    //点击2次返回，退出程序
    private boolean isExit = false;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        //设置一下ActionBar
        setSupportActionBar(toolbar);
        //设置一下ActionBar标题为空，否则默认显示应用名
        getSupportActionBar().setTitle("");
        init();
    }

    /*进入页面数据初始化,默认显示为是市场页面*/
    private void init(){
        //刚进来默认选择市场
        textViews[0].setSelected(true);
        if (CachePreferences.getUser().getName() == null) {
            viewPager.setAdapter(unLoginAdapter);
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setAdapter(loginAdapter);
            viewPager.setCurrentItem(0);
        }

        //viewpager添加滑动事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //遍历取消选择效果
                for (TextView textView : textViews){
                    textView.setSelected(false);
                }
                //更改title，设置选择效果
                tv_title.setText(textViews[position].getText());
                textViews[position].setSelected(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private FragmentStatePagerAdapter unLoginAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ShopFragment();
                case 1:
                    return new NoLoginFragment();
                case 2:
                    return new NoLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private FragmentStatePagerAdapter loginAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        case 0:
                            return new ShopFragment();
                        case 1:
                            // TODO: 2016/11/23 0023 接入环信后，待操作
                            return new NoLoginFragment();
                        case 2:
                            // TODO: 2016/11/23 0023 接入环信后，待操作
                            return new NoLoginFragment();
                        case 3:
                            return new MeFragment();
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 4;
                }
            };


    //textView点击事件
    @OnClick({R.id.tv_shop,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    public void onClick(TextView view){
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        view.setSelected(true);
        // 参数false代表瞬间切换，而不是平滑过渡
        viewPager.setCurrentItem((Integer) view.getTag(), false);
        tv_title.setText(textViews[(Integer) view.getTag()].getText());
    }

    //点击2次返回，退出程序
    @Override
    public void onBackPressed() {
        if (!isExit){
            isExit = true;
            activityUtils.showToast("再摁一次退出程序");
            //两秒内再次点击则退出
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            },2000);
        }else{
            finish();
        }
    }
}
