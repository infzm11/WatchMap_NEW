package com.android.hoinnet.highde.view.popView;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;

import com.android.hoinnet.highde.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RxPopupView {
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
    public static final int POSITION_ABOVE = 0;
    public static final int POSITION_BELOW = 1;
    public static final int POSITION_LEFT_TO = 3;
    public static final int POSITION_RIGHT_TO = 4;
    private int mAlign;
    private View mAnchorView;
    private boolean mArrow;
    private int mBackgroundColor;
    private Context mContext;
    private float mElevation;
    private String mMessage;
    private int mOffsetX;
    private int mOffsetY;
    private int mPosition;
    private ViewGroup mRootViewGroup;
    private Spannable mSpannableMessage;
    private int mTextColor;
    private int mTextGravity;
    private int mTextSize;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public int mAlign;
        /* access modifiers changed from: private */
        public View mAnchorView;
        /* access modifiers changed from: private */
        public boolean mArrow;
        /* access modifiers changed from: private */
        public int mBackgroundColor;
        /* access modifiers changed from: private */
        public Context mContext;
        /* access modifiers changed from: private */
        public float mElevation;
        /* access modifiers changed from: private */
        public String mMessage;
        /* access modifiers changed from: private */
        public int mOffsetX;
        /* access modifiers changed from: private */
        public int mOffsetY;
        /* access modifiers changed from: private */
        public int mPosition;
        /* access modifiers changed from: private */
        public ViewGroup mRootViewGroup;
        /* access modifiers changed from: private */
        public Spannable mSpannableMessage;
        /* access modifiers changed from: private */
        public int mTextColor;
        /* access modifiers changed from: private */
        public int mTextGravity;
        /* access modifiers changed from: private */
        public int mTextSize;

        public Builder(Context context, View view, ViewGroup viewGroup, String str, int i) {
            this.mContext = context;
            this.mAnchorView = view;
            this.mRootViewGroup = viewGroup;
            this.mMessage = str;
            this.mSpannableMessage = null;
            this.mPosition = i;
            this.mAlign = 0;
            this.mOffsetX = 0;
            this.mOffsetY = 0;
            this.mArrow = true;
            this.mBackgroundColor = context.getResources().getColor(R.color.colorBackground);
            this.mTextColor = context.getResources().getColor(R.color.colorText);
            this.mTextGravity = 0;
            this.mTextSize = 16;
        }

        public Builder(Context context, View view, ViewGroup viewGroup, Spannable spannable, int i) {
            this.mContext = context;
            this.mAnchorView = view;
            this.mRootViewGroup = viewGroup;
            this.mMessage = null;
            this.mSpannableMessage = spannable;
            this.mPosition = i;
            this.mAlign = 0;
            this.mOffsetX = 0;
            this.mOffsetY = 0;
            this.mArrow = true;
            this.mBackgroundColor = context.getResources().getColor(R.color.colorBackground);
            this.mTextColor = context.getResources().getColor(R.color.colorText);
            this.mTextGravity = 1;
            this.mTextSize = 14;
        }

        public Builder setPosition(int i) {
            this.mPosition = i;
            return this;
        }

        public Builder setAlign(int i) {
            this.mAlign = i;
            return this;
        }

        public Builder setOffsetX(int i) {
            this.mOffsetX = i;
            return this;
        }

        public Builder setOffsetY(int i) {
            this.mOffsetY = i;
            return this;
        }

        public Builder withArrow(boolean z) {
            this.mArrow = z;
            return this;
        }

        public Builder setBackgroundColor(int i) {
            this.mBackgroundColor = i;
            return this;
        }

        public Builder setTextColor(int i) {
            this.mTextColor = i;
            return this;
        }

        public Builder setElevation(float f) {
            this.mElevation = f;
            return this;
        }

        public Builder setGravity(int i) {
            this.mTextGravity = i;
            return this;
        }

        public Builder setTextSize(int i) {
            this.mTextSize = i;
            return this;
        }

        public RxPopupView build() {
            return new RxPopupView(this);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
    }

    public RxPopupView(Builder builder) {
        this.mContext = builder.mContext;
        this.mAnchorView = builder.mAnchorView;
        this.mRootViewGroup = builder.mRootViewGroup;
        this.mMessage = builder.mMessage;
        this.mPosition = builder.mPosition;
        this.mAlign = builder.mAlign;
        this.mOffsetX = builder.mOffsetX;
        this.mOffsetY = builder.mOffsetY;
        this.mArrow = builder.mArrow;
        this.mBackgroundColor = builder.mBackgroundColor;
        this.mTextColor = builder.mTextColor;
        this.mElevation = builder.mElevation;
        this.mTextGravity = builder.mTextGravity;
        this.mSpannableMessage = builder.mSpannableMessage;
        this.mTextSize = builder.mTextSize;
    }

    public Context getContext() {
        return this.mContext;
    }

    public View getAnchorView() {
        return this.mAnchorView;
    }

    public ViewGroup getRootView() {
        return this.mRootViewGroup;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public int getAlign() {
        return this.mAlign;
    }

    public int getOffsetX() {
        return this.mOffsetX;
    }

    public int getOffsetY() {
        return this.mOffsetY;
    }

    public boolean hideArrow() {
        return !this.mArrow;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public boolean positionedLeftTo() {
        return 3 == this.mPosition;
    }

    public boolean positionedRightTo() {
        return 4 == this.mPosition;
    }

    public boolean positionedAbove() {
        return this.mPosition == 0;
    }

    public boolean positionedBelow() {
        return 1 == this.mPosition;
    }

    public boolean alignedCenter() {
        return this.mAlign == 0;
    }

    public boolean alignedLeft() {
        return 1 == this.mAlign;
    }

    public boolean alignedRight() {
        return 2 == this.mAlign;
    }

    public float getElevation() {
        return this.mElevation;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    public int getTextGravity() {
        switch (this.mTextGravity) {
            case 1:
                return GravityCompat.START;
            case 2:
                return GravityCompat.END;
            default:
                return 17;
        }
    }

    public Spannable getSpannableMessage() {
        return this.mSpannableMessage;
    }
}
