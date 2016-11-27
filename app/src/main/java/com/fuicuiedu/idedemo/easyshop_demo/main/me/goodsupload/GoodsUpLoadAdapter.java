package com.fuicuiedu.idedemo.easyshop_demo.main.me.goodsupload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.model.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsUpLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //表示编辑时的模式，1为普通，2为可选
    public static final int MODE_NORMAL = 1;
    public static final int MODE_MULTI_SELECT = 2;
    //代表图片编辑的模式
    public int mode;

    //用枚举，表示item类型，是有图或者待添加
    public enum ITEM_TYPE {ITEM_NORMAl, ITEM_ADD}

    private ArrayList<ImageItem> list = new ArrayList<>();
    private final LayoutInflater inflater;
    private OnItemClickedListener mListener;

    public GoodsUpLoadAdapter(Context context, ArrayList<ImageItem> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    //添加图片
    public void add(ImageItem photo) {
        list.add(photo);
    }

    public int getSize() {
        return list.size();
    }

    public ArrayList<ImageItem> getList() {
        return list;
    }

    //刷新数据
    public void notifyData() {
        notifyDataSetChanged();
    }

    public void notifyDataSet() {
        notifyDataSetChanged();
    }

    //模式选择，增加或者删除
    @SuppressWarnings("SameParameterValue")
    public void changeMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    //获取当前模式，增加或者删除
    public int getMode() {
        return mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前显示的item类型，有图或者待添加，从而选择不同布局，不同的viewholder
        if (viewType == ITEM_TYPE.ITEM_NORMAl.ordinal()) {
            return new ItemSelectViewHolder(inflater.inflate(R.layout.layout_item_recyclerview, parent, false));
        } else {
            return new ItemAddViewHolder(inflater.inflate(R.layout.layout_item_recyclerviewlast, parent, false));
        }
    }

    //获取item类型方法中，去判断item类型，从而加载不同布局
    @Override
    public int getItemViewType(int position) {
        //position与图片数量相同时，则为添加新图片的布局
        if (position == list.size()) return ITEM_TYPE.ITEM_ADD.ordinal();
        return ITEM_TYPE.ITEM_NORMAl.ordinal();
    }

    @SuppressLint("RecyclerView")
    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //如果当前的viewholder是“选择holder”的实例
        if (holder instanceof ItemSelectViewHolder) {
            final ImageItem photo = list.get(position);
            final ItemSelectViewHolder item_select = (ItemSelectViewHolder) holder;
            item_select.photo = photo;
            //如果选择编辑模式
            if (mode == MODE_MULTI_SELECT) {
                //勾选框可见
                item_select.checkBox.setVisibility(View.VISIBLE);
                //勾选框设置选择监听
                item_select.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //imagerItem中已选择属性改变
                        list.get(position).setIsCheck(isChecked);
                    }
                });
                //勾选框改变（根据imageItem的选择属性）
                item_select.checkBox.setChecked(photo.isCheck());
            } else if (mode == MODE_NORMAL) {
                item_select.checkBox.setVisibility(View.GONE);
            }
            //图片设置
            item_select.ivPhoto.setImageBitmap(photo.getBitmap());
            /*图片长按事件的监听*/
            item_select.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //模式改为可勾选模式
                    mode = MODE_MULTI_SELECT;
                    //更新
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.onLongClicked();
                    }
                    return false;
                }
            });
            /*图片单击事件的监听*/
            item_select.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPhotoClicked(item_select.photo, item_select.ivPhoto);
                    }
                }
            });
        }
        //当前holder是“增加图片Holder”的实例
        else if (holder instanceof ItemAddViewHolder) {
            final ItemAddViewHolder item_add = (ItemAddViewHolder) holder;
            //最多加八张图，判断
            if (position == 8) {
                item_add.ib_add.setVisibility(View.GONE);
            } else {
                item_add.ib_add.setVisibility(View.VISIBLE);
            }
            //点击添加图片
            item_add.ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onAddClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size() + 1, 8);
    }

    /**
     * RecycleView中item点击事件
     */
    public interface OnItemClickedListener {

        /**
         * 单击图片的监听事件
         *
         * @param photo     {@link ImageItem}
         * @param imageView 点击图片对应的ImageView
         */
        void onPhotoClicked(ImageItem photo, ImageView imageView);

        /**
         * 添加按钮的监听事件
         */
        void onAddClicked();

        /**
         * 长按照片的监听事件
         */
        void onLongClicked();
    }

    /**
     * 图片布局
     */
    public static class ItemSelectViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.cb_check_photo)
        CheckBox checkBox;
        ImageItem photo;

        public ItemSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 商品图片添加按钮布局
     */
    public static class ItemAddViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ib_recycle_add)
        ImageButton ib_add;

        public ItemAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
