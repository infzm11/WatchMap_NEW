package com.jcodecraeer.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.hoinnet.highde.R;
import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;
import java.util.Date;

public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {
    private static final int ROTATE_ANIM_DURATION = 180;
    private ImageView mArrowImageView;
    private LinearLayout mContainer;
    private TextView mHeaderTimeView;
    public int mMeasuredHeight;
    private SimpleViewSwitcher mProgressBar;
    private Animation mRotateDownAnim;
    private Animation mRotateUpAnim;
    private int mState = 0;
    private TextView mStatusTextView;

    public ArrowRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public ArrowRefreshHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        this.mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_header, (ViewGroup) null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(layoutParams);
        setPadding(0, 0, 0, 0);
        addView(this.mContainer, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(80);
        this.mArrowImageView = (ImageView) findViewById(R.id.listview_header_arrow);
        this.mStatusTextView = (TextView) findViewById(R.id.refresh_status_textview);
        this.mProgressBar = (SimpleViewSwitcher) findViewById(R.id.listview_header_progressbar);
        AVLoadingIndicatorView aVLoadingIndicatorView = new AVLoadingIndicatorView(getContext());
        aVLoadingIndicatorView.setIndicatorColor(-4868683);
        aVLoadingIndicatorView.setIndicatorId(22);
        this.mProgressBar.setView(aVLoadingIndicatorView);
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateUpAnim = rotateAnimation;
        this.mRotateUpAnim.setDuration(180);
        this.mRotateUpAnim.setFillAfter(true);
        RotateAnimation rotateAnimation2 = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateDownAnim = rotateAnimation2;
        this.mRotateDownAnim.setDuration(180);
        this.mRotateDownAnim.setFillAfter(true);
        this.mHeaderTimeView = (TextView) findViewById(R.id.last_refresh_time);
        measure(-2, -2);
        this.mMeasuredHeight = getMeasuredHeight();
    }

    public void setProgressStyle(int i) {
        if (i == -1) {
            this.mProgressBar.setView(new ProgressBar(getContext(), (AttributeSet) null, 16842871));
            return;
        }
        AVLoadingIndicatorView aVLoadingIndicatorView = new AVLoadingIndicatorView(getContext());
        aVLoadingIndicatorView.setIndicatorColor(-4868683);
        aVLoadingIndicatorView.setIndicatorId(i);
        this.mProgressBar.setView(aVLoadingIndicatorView);
    }

    public void setArrowImageView(int i) {
        this.mArrowImageView.setImageResource(i);
    }

    public void setState(int i) {
        if (i != this.mState) {
            if (i == 2) {
                this.mArrowImageView.clearAnimation();
                this.mArrowImageView.setVisibility(INVISIBLE);
                this.mProgressBar.setVisibility(VISIBLE);
                smoothScrollTo(this.mMeasuredHeight);
            } else if (i == 3) {
                this.mArrowImageView.setVisibility(INVISIBLE);
                this.mProgressBar.setVisibility(INVISIBLE);
            } else {
                this.mArrowImageView.setVisibility(VISIBLE);
                this.mProgressBar.setVisibility(INVISIBLE);
            }
            switch (i) {
                case 0:
                    if (this.mState == 1) {
                        this.mArrowImageView.startAnimation(this.mRotateDownAnim);
                    }
                    if (this.mState == 2) {
                        this.mArrowImageView.clearAnimation();
                    }
                    this.mStatusTextView.setText(R.string.listview_header_hint_normal);
                    break;
                case 1:
                    if (this.mState != 1) {
                        this.mArrowImageView.clearAnimation();
                        this.mArrowImageView.startAnimation(this.mRotateUpAnim);
                        this.mStatusTextView.setText(R.string.listview_header_hint_release);
                        break;
                    }
                    break;
                case 2:
                    this.mStatusTextView.setText(R.string.refreshing);
                    break;
                case 3:
                    this.mStatusTextView.setText(R.string.refresh_done);
                    break;
            }
            this.mState = i;
        }
    }

    public int getState() {
        return this.mState;
    }

    public void refreshComplete() {
        this.mHeaderTimeView.setText(friendlyTime(new Date()));
        setState(3);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ArrowRefreshHeader.this.reset();
            }
        }, 200);
    }

    public void setVisibleHeight(int i) {
        if (i < 0) {
            i = 0;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mContainer.getLayoutParams();
        layoutParams.height = i;
        this.mContainer.setLayoutParams(layoutParams);
    }

    public int getVisibleHeight() {
        return ((LinearLayout.LayoutParams) this.mContainer.getLayoutParams()).height;
    }

    public void onMove(float f) {
        if (getVisibleHeight() > 0 || f > 0.0f) {
            setVisibleHeight(((int) f) + getVisibleHeight());
            if (this.mState > 1) {
                return;
            }
            if (getVisibleHeight() > this.mMeasuredHeight) {
                setState(1);
            } else {
                setState(0);
            }
        }
    }

    public boolean releaseAction() {
        boolean z;
        int visibleHeight = getVisibleHeight();
        if (getVisibleHeight() <= this.mMeasuredHeight || this.mState >= 2) {
            z = false;
        } else {
            setState(2);
            z = true;
        }
        if (this.mState == 2) {
            int i = this.mMeasuredHeight;
        }
        if (this.mState != 2) {
            smoothScrollTo(0);
        }
        return z;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ArrowRefreshHeader.this.setState(0);
            }
        }, 500);
    }

    private void smoothScrollTo(int i) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{getVisibleHeight(), i});
        ofInt.setDuration(300).start();
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArrowRefreshHeader.this.setVisibleHeight(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        ofInt.start();
    }

    public static String friendlyTime(Date date) {
        int currentTimeMillis = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
        if (currentTimeMillis == 0) {
            return "刚刚";
        }
        if (currentTimeMillis > 0 && currentTimeMillis < 60) {
            return currentTimeMillis + "秒前";
        } else if (currentTimeMillis >= 60 && currentTimeMillis < 3600) {
            return Math.max(currentTimeMillis / 60, 1) + "分钟前";
        } else if (currentTimeMillis >= 3600 && currentTimeMillis < 86400) {
            return (currentTimeMillis / 3600) + "小时前";
        } else if (currentTimeMillis >= 86400 && currentTimeMillis < 2592000) {
            return (currentTimeMillis / 86400) + "天前";
        } else if (currentTimeMillis < 2592000 || currentTimeMillis >= 31104000) {
            return (currentTimeMillis / 31104000) + "年前";
        } else {
            return (currentTimeMillis / 2592000) + "月前";
        }
    }
}
