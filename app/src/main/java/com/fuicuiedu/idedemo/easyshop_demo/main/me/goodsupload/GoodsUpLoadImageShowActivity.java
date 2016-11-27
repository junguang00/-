package com.fuicuiedu.idedemo.easyshop_demo.main.me.goodsupload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fuicuiedu.idedemo.easyshop_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示商品上传图片的详情页
 */
public class GoodsUpLoadImageShowActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_goods_phone)
    ImageView iv_goods_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load_image_show);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bitmap bitmap = getIntent().getParcelableExtra("images");
        int mWidth = getIntent().getIntExtra("width", 0);
        int mHeight = getIntent().getIntExtra("height", 0);

        iv_goods_phone.setMaxWidth(mWidth);
        iv_goods_phone.setMaxHeight(mHeight);
        iv_goods_phone.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
