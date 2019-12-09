package com.android.hoinnet.highde.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hoinnet.highde.R;

public class LeftRightButton extends LinearLayout implements View.OnClickListener {
    private final int BUTTON_TYPE_IMG;
    private Context mContext;
    private LinearLayout mLayoutLeft;
    private LinearLayout mLayoutRight;
    private Drawable mLeftDrawableBg;
    private Drawable mLeftDrawableImg;
    private ImageView mLeftImg;
    private LeftRightListener mLeftRightListener;
    private String mLeftText;
    private TextView mLeftTv;
    private Resources mRes;
    private Drawable mRightDrawableBg;
    private Drawable mRightDrawableImg;
    private ImageView mRightImg;
    private String mRightText;
    private TextView mRightTv;
    private int mType;
    private final int DEFAULT_TYPE_VALUE = 1;

    public interface LeftRightListener {
        void onLeftClick(View view);

        void onRightClick(View view);
    }

    public LeftRightButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public LeftRightButton(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LeftRightButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.BUTTON_TYPE_IMG = 1;
        this.mType = 1;
        this.mContext = context;
        this.mRes = getResources();
        LayoutInflater.from(context).inflate(R.layout.left_right_layout, this, true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LeftRightView);
        this.mLeftDrawableBg = obtainStyledAttributes.getDrawable(R.styleable.LeftRightView_leftBackground);
        this.mRightDrawableBg = obtainStyledAttributes.getDrawable(R.styleable.LeftRightView_rightBackground);
        this.mType = obtainStyledAttributes.getInt(R.styleable.LeftRightView_leftRightType, DEFAULT_TYPE_VALUE);
        this.mLeftDrawableImg = obtainStyledAttributes.getDrawable(R.styleable.LeftRightView_leftImgSrc);
        this.mRightDrawableImg = obtainStyledAttributes.getDrawable(R.styleable.LeftRightView_rightImgSrc);
        this.mLeftText = obtainStyledAttributes.getString(R.styleable.LeftRightView_leftText);
        this.mRightText = obtainStyledAttributes.getString(R.styleable.LeftRightView_rightText);
        obtainStyledAttributes.recycle();
        this.mLayoutLeft = (LinearLayout) findViewById(R.id.lr_cancel);
        this.mLayoutRight = (LinearLayout) findViewById(R.id.lr_confrim);
        this.mLayoutLeft.setBackground(this.mLeftDrawableBg);
        this.mLayoutRight.setBackground(this.mRightDrawableBg);
        this.mLeftImg = (ImageView) findViewById(R.id.lr_left_img);
        this.mRightImg = (ImageView) findViewById(R.id.lr_right_img);
        this.mLeftTv = (TextView) findViewById(R.id.lr_left_tv);
        this.mRightTv = (TextView) findViewById(R.id.lr_right_tv);
        if (this.mType == DEFAULT_TYPE_VALUE) {
            this.mLeftTv.setVisibility(GONE);
            this.mRightTv.setVisibility(GONE);
            this.mLeftImg.setVisibility(VISIBLE);
            this.mRightImg.setVisibility(VISIBLE);
            this.mLeftImg.setImageDrawable(this.mLeftDrawableImg);
            this.mRightImg.setImageDrawable(this.mRightDrawableImg);
        } else {
            this.mLeftTv.setVisibility(VISIBLE);
            this.mRightTv.setVisibility(VISIBLE);
            this.mLeftImg.setVisibility(GONE);
            this.mRightImg.setVisibility(GONE);
            this.mLeftTv.setText(this.mLeftText);
            this.mRightTv.setText(this.mRightText);
        }
        this.mLayoutLeft.setOnClickListener(this);
        this.mLayoutRight.setOnClickListener(this);
    }

    public void setLeftRightListener(LeftRightListener leftRightListener) {
        if (leftRightListener != null) {
            this.mLeftRightListener = leftRightListener;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lr_cancel:
                if (this.mLeftRightListener != null) {
                    this.mLeftRightListener.onLeftClick(view);
                    return;
                }
                return;
            case R.id.lr_confrim:
                if (this.mLeftRightListener != null) {
                    this.mLeftRightListener.onRightClick(view);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
