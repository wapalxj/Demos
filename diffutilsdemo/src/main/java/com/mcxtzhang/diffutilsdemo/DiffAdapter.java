package com.mcxtzhang.diffutilsdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 介绍：普普通的adapter，
 * 但是 唯一亮点~
 * public void onBindViewHolder(DiffVH holder, int position, List<Object> payloads)
 * 重写这个方法
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/9/12.
 */

public class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.DiffVH> {
    private final static String TAG = "zxt";
    private List<TestBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public DiffAdapter(Context mContext, List<TestBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<TestBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public DiffVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiffVH(mInflater.inflate(R.layout.item_diff, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiffVH holder, final int position) {
        TestBean bean = mDatas.get(position);
        holder.tv1.setText(bean.getName());
        holder.tv2.setText(bean.getDesc());
        holder.iv.setImageResource(bean.getPic());
        if (position ==2){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            Log.d(TAG, "实验 getAdapterPosition 和 getLayoutPosition 不一样的时候： position = [" + position + "], payloads = ["  + "]");
                            Log.d(TAG, "实验 getAdapterPosition 和 getLayoutPosition 不一样的时候： holder.getAdapterPosition() = [" + holder.getAdapterPosition() + "], payloads = ["  + "]");
                            Log.d(TAG, "实验 getAdapterPosition 和 getLayoutPosition 不一样的时候：  holder.getLayoutPosition() = [" + holder.getLayoutPosition() + "], payloads = ["  + "]");
                            //Log.d(TAG, " holder.getPosition() = [" + holder.getPosition() + "], payloads = ["  + "]");
                            //Log.d(TAG, "holder.getOldPosition() = [" + holder.getOldPosition() + "], payloads = ["  + "]");

                            Thread.sleep(1);

                        } catch (InterruptedException e) {
                            Log.e(TAG, "run: error");
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "], payloads = ["  + "]");
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getAdapterPosition() = [" + holder.getAdapterPosition() + "], payloads = ["  + "]");
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getLayoutPosition() = [" + holder.getLayoutPosition() + "], payloads = ["  + "]");
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getPosition() = [" + holder.getPosition() + "], payloads = ["  + "]");
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getOldPosition() = [" + holder.getOldPosition() + "], payloads = ["  + "]");

            }
        });
    }

    @Override
    public void onBindViewHolder(DiffVH holder, int position, List<Object> payloads) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "], payloads = ["  + "]");
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getAdapterPosition() = [" + holder.getAdapterPosition() + "], payloads = ["  + "]");
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getLayoutPosition() = [" + holder.getLayoutPosition() + "], payloads = ["  + "]");
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getPosition() = [" + holder.getPosition() + "], payloads = ["  + "]");
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], holder.getOldPosition() = [" + holder.getOldPosition() + "], payloads = ["  + "]");

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //文艺青年中的文青
            Bundle payload = (Bundle) payloads.get(0);
            TestBean bean = mDatas.get(position);
            for (String key : payload.keySet()) {
                switch (key) {
                    case "KEY_DESC":
                        //这里可以用payload里的数据，不过data也是新的 也可以用
                        holder.tv2.setText(bean.getDesc());
                        break;
                    case "KEY_PIC":
                        holder.iv.setImageResource(payload.getInt(key));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class DiffVH extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView iv;

        public DiffVH(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}