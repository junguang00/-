package com.fuicuiedu.idedemo.easyshop_demo.main.me.goodsupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ImageUtils;
import com.fuicuiedu.idedemo.easyshop_demo.commons.MyFileUtils;
import com.fuicuiedu.idedemo.easyshop_demo.components.PicWindow;
import com.fuicuiedu.idedemo.easyshop_demo.components.ProgressDialogFragment;
import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsUpLoad;
import com.fuicuiedu.idedemo.easyshop_demo.model.ImageItem;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsUpLoadActivity extends MvpActivity<GoodsUpLoadView,GoodsUpLoadPresenter> implements GoodsUpLoadView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_goods_name)
    EditText et_goods_name;
    @BindView(R.id.et_goods_price)
    EditText et_goods_price;
    @BindView(R.id.et_goods_describe)
    EditText et_goods_describe;
    @BindView(R.id.tv_goods_type)
    TextView tv_goods_type;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.btn_goods_load)
    Button btn_goods_load;

    private final String[] goods_type = {"家用", "电子", "服饰", "玩具", "图书", "礼品", "其它"};
    /*商品种类为自定义*/
    private final String[] goods_type_num = {"household", "electron", "dress", "toy", "book", "gift", "other"};

    private ActivityUtils activityUtils;
    private String str_goods_name;
    private String str_goods_price;
    private String str_goods_type = goods_type_num[0];
    private String str_goods_describe;

    /*状态1普通*/
    public static final int MODE_DONE = 1;
    /*状态2删除*/
    public static final int MODE_DELETE = 2;
    private int title_mode = MODE_DONE;
    private ArrayList<ImageItem> list = new ArrayList<>();
    private GoodsUpLoadAdapter adapter;
    private PicWindow picWindow;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load);
        activityUtils = new ActivityUtils(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        viewContent();
    }

    @NonNull
    @Override
    public GoodsUpLoadPresenter createPresenter() {
        return new GoodsUpLoadPresenter();
    }

    /*对popuWindow和RecyclerView做初始化*/
    private void initView() {
        picWindow = new PicWindow(this, listener);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        // 设置ItemAnimator,默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小，如果你没有设置setHasFixedSized没有设置的代价将会是非常昂贵的。
        // 因为RecyclerView会需要而外计算每个item的size。
        // 当不是瀑布流时，设置这个可以避免重复的增删造成而外的浪费资源
        recyclerView.setHasFixedSize(true);

        list = getFilePhoto();
        adapter = new GoodsUpLoadAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setListener(itemClickedListener);
    }

    /*图像选择弹窗内的监听事件*/
    private final PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            /*从相册选择*/
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            /*从相机选择*/
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    /*图片的点击事件*/
    private GoodsUpLoadAdapter.OnItemClickedListener itemClickedListener
            = new GoodsUpLoadAdapter.OnItemClickedListener() {
        @Override
        public void onPhotoClicked(ImageItem photo, ImageView imageView) {
            //跳转到图片展示页
            Intent intent = new Intent(GoodsUpLoadActivity.this, GoodsUpLoadImageShowActivity.class);
            intent.putExtra("images", photo.getBitmap());//必须
            intent.putExtra("width", imageView.getWidth());//必须
            intent.putExtra("height", imageView.getHeight());//必须
            startActivity(intent);
        }

        @Override
        public void onAddClicked() {
            //展示图片来源的pop
            if (picWindow != null && picWindow.isShowing()) {
                picWindow.dismiss();
            } else if (picWindow != null) {
                picWindow.show();
            }
        }

        @Override
        public void onLongClicked() {
            //模式改为删除模式
            title_mode = MODE_DELETE;
            //删除的问题可见
            tv_goods_delete.setVisibility(View.VISIBLE);
        }
    };

    /*获取商品名称价格描述信息并监听*/
    private void viewContent() {
        et_goods_name.addTextChangedListener(textWatcher);
        et_goods_price.addTextChangedListener(textWatcher);
        et_goods_describe.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            str_goods_name = et_goods_name.getText().toString();
            str_goods_price = et_goods_price.getText().toString();
            str_goods_describe = et_goods_describe.getText().toString();
            boolean can_save = !(TextUtils.isEmpty(str_goods_name) ||
                    TextUtils.isEmpty(str_goods_price) || TextUtils.isEmpty(str_goods_describe));
            btn_goods_load.setEnabled(can_save);
        }
    };

    //返回需要实现的方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (title_mode == MODE_DONE) {
            deleteCache();
            finish();
        } else if (title_mode == MODE_DELETE)
            changeModeOnActivity();
    }

    @Override
    protected void onDestroy() {
        if (cropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
        deleteCache();
        super.onDestroy();
    }

    /*图片裁剪*/
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //文件名就用系统的当前时间，不重复
            String fileName = String.valueOf(System.currentTimeMillis());
            //通过工具类拿到一个bitmap
            Bitmap bitmap = ImageUtils.readDownsampledImage(uri.getPath(), 1080, 1920);
            //将小图保存到SD卡中
            MyFileUtils.saveBitmap(bitmap, fileName);
            //将item添加到适配器中
            ImageItem take_photo = new ImageItem();
            take_photo.setImagePath(fileName + ".JPEG");
            take_photo.setBitmap(bitmap);
            adapter.add(take_photo);
            adapter.notifyDataSet();
        }

        @Override
        public void onCropCancel() {
        }

        @Override
        public void onCropFailed(String message) {
        }

        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return GoodsUpLoadActivity.this;
        }
    };

    //当activity拿到返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 帮助我们去处理结果(剪切完的图像)
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    /**
     * 获取缓存文件夹中文件
     *
     * @return {@link ImageItem} 列表
     */
    private ArrayList<ImageItem> getFilePhoto() {
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        File[] files = new File(MyFileUtils.SD_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                Bitmap bitmap = BitmapFactory.decodeFile(MyFileUtils.SD_PATH + file.getName());
                ImageItem image = new ImageItem();
                image.setImagePath(file.getName());
                image.setBitmap(bitmap);
                imageItems.add(image);
            }
        }
        return imageItems;
    }

    /**
     * 按返回键改变适配中布局的状态|改变标题删除按钮状态|改变适配器中checkbox的选择状态
     */
    private void changeModeOnActivity() {
        //判断，当前模式是否是删除模式
        if (adapter.getMode() == GoodsUpLoadAdapter.MODE_MULTI_SELECT) {
            tv_goods_delete.setVisibility(View.GONE);
            title_mode = MODE_DONE;
            adapter.changeMode(GoodsUpLoadAdapter.MODE_NORMAL);
            for (int i = 0; i < adapter.getList().size(); i++) {
                adapter.getList().get(i).setIsCheck(false);
            }
        }
    }

    /**
     * 删除缓存文件夹中的文件
     */
    private void deleteCache() {
        for (int i = 0; i < adapter.getList().size(); i++) {
            MyFileUtils.delFile(adapter.getList().get(i).getImagePath());
        }
    }

    /**
     * 对商品信息初始化
     */
    private GoodsUpLoad setGoodInfo() {
        GoodsUpLoad goodsLoad = new GoodsUpLoad();
        goodsLoad.setName(str_goods_name);
        goodsLoad.setPrice(str_goods_price);
        goodsLoad.setDescribe(str_goods_describe);
        goodsLoad.setType(str_goods_type);
        goodsLoad.setMaster(CachePreferences.getUser().getName());
        return goodsLoad;
    }

    @OnClick({R.id.btn_goods_type, R.id.tv_goods_delete, R.id.btn_goods_load})
    public void onClick(View view) {
        switch (view.getId()) {
            //商品类型选择
            case R.id.btn_goods_type:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("商品类型");
                builder.setItems(goods_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_goods_type.setText(goods_type[which]);
                        str_goods_type = goods_type_num[which];
                    }
                });
                builder.create().show();
                break;
            //删除
            case R.id.tv_goods_delete:
                ArrayList<ImageItem> del_list = adapter.getList();
                int num = del_list.size();
                for (int i = num - 1; i >= 0; i--) {
                    if (del_list.get(i).isCheck()) {
                        MyFileUtils.delFile(del_list.get(i).getImagePath());
                        del_list.remove(i);
                    }
                }
                this.list = del_list;
                adapter.notifyData();
                changeModeOnActivity();
                title_mode = MODE_DONE;
                break;
            //商品上传
            case R.id.btn_goods_load:
                if (adapter.getSize() == 0) {
                    activityUtils.showToast("最少有一张商品图片！");
                    return;
                }
                presenter.upload(setGoodInfo(), list);
                break;
        }
    }

    @Override
    public void upLoadSuccess() {
        finish();
    }

    @Override
    public void showProgress() {
        if (dialogFragment == null)
            dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        dialogFragment.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
