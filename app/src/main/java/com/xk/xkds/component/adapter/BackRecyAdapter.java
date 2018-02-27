package com.xk.xkds.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xk.xkds.R;
import com.xk.xkds.entity.ADBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  LJP on 2017/11/23.
 */

public class BackRecyAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ADBean.ListBean> listBeen =new ArrayList<>();

    public BackRecyAdapter (Context context, List<ADBean.ListBean> listBeen){
        this.context = context;
        this.listBeen.clear();
        this.listBeen.addAll(listBeen);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_back_rcv, parent, false);
        BackHoder backHodel = new BackHoder(inflate);
        return backHodel;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BackHoder backHoder = (BackHoder) holder;
        Glide.with(context).load(listBeen.get(position).getImageBg()).into(backHoder.ivAppIcon);
        backHoder.ivAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }
    class  BackHoder extends RecyclerView.ViewHolder {

        private final ImageView ivAppIcon;

        public BackHoder(View itemView) {
            super(itemView);
            ivAppIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
        }
    }
    public interface  OnItemClick{
      void   onItemClick(int position);
    }
    private  OnItemClick onItemClickListener;
    public void setOnItemClickListener(OnItemClick onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
}
