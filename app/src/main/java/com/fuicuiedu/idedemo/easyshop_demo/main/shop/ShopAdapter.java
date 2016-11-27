package com.fuicuiedu.idedemo.easyshop_demo.main.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.components.AvatarLoadOptions;
import com.fuicuiedu.idedemo.easyshop_demo.model.GoodsInfo;
import com.fuicuiedu.idedemo.easyshop_demo.network.EasyShopApi;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    //所需数据
    private List<GoodsInfo> list = new ArrayList<>();
    //用来替换字符串
    private Context context;

    //添加数据
    public void addData(List<GoodsInfo> data){
        list.addAll(data);
        //通知更新
        notifyDataSetChanged();
    }

    //删除数据
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false);
        ShopViewHolder viewHolder = new ShopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        //商品名称
        holder.tv_name.setText(list.get(position).getName());
        //商品价格，替换字符串
        String price = context.getString(R.string.goods_money,list.get(position).getPrice());
        holder.tv_price.setText(price);
        //图片加载
        ImageLoader.getInstance()
                .displayImage(EasyShopApi.IMAGE_URL + list.get(position).getPage(),
                        holder.imageView, AvatarLoadOptions.build_item());

        //给图片设置点击事件
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onPhotoClicked(list.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_item_recycler)
        ImageView imageView;
        @BindView(R.id.tv_item_name)
        TextView tv_name;
        @BindView(R.id.tv_item_price)
        TextView tv_price;


        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //点击图片事件监听
    public interface onItemClickedListener{
        //点击事件，并且把商品信息传给我
        void onPhotoClicked(GoodsInfo goodsInfo);
    }

    private onItemClickedListener listener;

    //对外公开方法
    public void setListener(onItemClickedListener listener){
        this.listener = listener;
    }
}
