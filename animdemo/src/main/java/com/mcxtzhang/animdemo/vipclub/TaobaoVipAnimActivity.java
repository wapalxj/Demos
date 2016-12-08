package com.mcxtzhang.animdemo.vipclub;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcxtzhang.animdemo.R;

import java.util.ArrayList;
import java.util.List;

public class TaobaoVipAnimActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    HorizontalScrollView mScrollView;
    LinearLayout mLlcontainer;
    ImageView mAvatar;

    List<ProgressBar> animPbs;
    private List<VipLevelBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao_vip_anim);
        mScrollView = (HorizontalScrollView) findViewById(R.id.hsv);
        mLlcontainer = (LinearLayout) findViewById(R.id.llContainer);
        mLlcontainer.post(new Runnable() {
            @Override
            public void run() {
                animPbs = new ArrayList<ProgressBar>();

                ViewUtils.addViews(mLlcontainer, new BaseVgAdapter<VipLevelBean>(mDatas = initDatas(), TaobaoVipAnimActivity.this) {
                    @Override
                    public View getView(ViewGroup parent, int pos, VipLevelBean data) {
                        View itemView;
                        TextView tvLevel;
                        ProgressBar pb;
                        if (data.isCurrent()) {
                            itemView = mInflater.inflate(R.layout.item_current, parent, false);
                            mAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
                        } else {
                            itemView = mInflater.inflate(R.layout.item_normal, parent, false);
                        }
                        tvLevel = (TextView) itemView.findViewById(R.id.tvLevel);
                        pb = (ProgressBar) itemView.findViewById(R.id.pb);


                        animPbs.add(pb);
                        /*if (pos == getCount() - 1) {
                            //开始动画
                            beginAnim(animPbs, mScrollView, mDatas, mAvatar);
                        }*/

                        tvLevel.setText(data.getLevel() + "");
                        return itemView;
                    }
                }, true);
            }
        });

        findViewById(R.id.btnAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始动画
                beginAnim(animPbs, mScrollView, mDatas, mAvatar);
            }
        });
    }

    private void beginAnim(final List<ProgressBar> animPbs, final HorizontalScrollView horizontalScrollView, List<VipLevelBean> datas, ImageView avatar) {
        //动画分两部分，一分部是 进度条的填充，另一部分是ScrollView的滚动
        int size = animPbs.size();
        AnimatorSet set = new AnimatorSet();
        List<Animator> anims = new ArrayList<Animator>(size);
        int perAnimTime = 500;
        for (int i = 0; i < size; i++) {
            ValueAnimator anim = ValueAnimator.ofInt(0, 100);
            final int finalI = i;
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ProgressBar pb = animPbs.get(finalI);
                    pb.setProgress((Integer) animation.getAnimatedValue());

                    /*horizontalScrollView.scrollTo(pb.getWidth() * finalI, 0);*/
                    //horizontalScrollView.scrollTo((int) (pb.getWidth() * finalI + ((float) animation.getAnimatedValue() / 100) * pb.getWidth()), 0);
                }
            });
            anims.add(anim);
        }
        set.playSequentially(anims);
        set.setDuration(perAnimTime);


        //滚动ScrollView 至当前等级头像在屏幕中央

        int left = 0;
        //先找到当前等级
        int measuredWidth = mLlcontainer.getChildAt(0).getMeasuredWidth();
        Log.d(TAG, "beginAnim() called with: animPbs = [" + measuredWidth);

        int curLevel = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCurrent()) {
                curLevel = i;
                left = measuredWidth * i;
            }
        }

        /*left = avatar.getLeft();
        Log.d(TAG, "beginAnim() called with: left = [" + left);*/
        int width = avatar.getMeasuredWidth();
        int target = left + width / 2;
        Log.d(TAG, "beginAnim() called with: target = [" + target);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, "beginAnim() called with: screenWidth = [" + screenWidth);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, target - screenWidth / 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScrollView.scrollTo((Integer) animation.getAnimatedValue(),0);
            }
        });
        valueAnimator.setDuration(curLevel * perAnimTime);

        valueAnimator.start();
        set.start();
    }


    public List initDatas() {
        List<VipLevelBean> datas = new ArrayList<>();
        datas.add(new VipLevelBean(1));
        datas.add(new VipLevelBean(2));
        datas.add(new VipLevelBean(3));
        datas.add(new VipLevelBean(4));
        datas.add(new VipLevelBean(5).setCurrent(true));
        datas.add(new VipLevelBean(6));
        datas.add(new VipLevelBean(7));
        datas.add(new VipLevelBean(8));
        return datas;
    }
}