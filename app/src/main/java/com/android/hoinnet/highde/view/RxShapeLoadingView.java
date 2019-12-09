package com.android.hoinnet.highde.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.hoinnet.highde.R;

public class RxShapeLoadingView extends FrameLayout {
    private static final int ANIMATION_DURATION = 250;
    private static float mDistance = 200.0f;
    public float factor = 1.2f;
    private AnimatorSet mAnimatorSet = null;
    private Runnable mFreeFallRunnable = new Runnable() {
        public void run() {
            RxShapeLoadingView.this.freeFall();
        }
    };
    private ImageView mIndicationIm;
    private String mLoadText;
    private TextView mLoadTextView;
    /* access modifiers changed from: private */
    public RxShapeView mRxShapeView;
    private int mTextAppearance;

    public RxShapeLoadingView(Context context) {
        super(context);
    }

    public RxShapeLoadingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RxShapeLoadingView);
        this.mLoadText = obtainStyledAttributes.getString(0);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(1, -1);
        obtainStyledAttributes.recycle();
    }

    public RxShapeLoadingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public int dip2px(float f) {
        return (int) ((f * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shape_loading_view1, (ViewGroup) null);
        mDistance = (float) dip2px(54.0f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.mRxShapeView = (RxShapeView) inflate.findViewById(R.id.shapeLoadingView);
        this.mIndicationIm = (ImageView) inflate.findViewById(R.id.indication);
        this.mLoadTextView = (TextView) inflate.findViewById(R.id.promptTV);
        if (this.mTextAppearance != -1) {
            this.mLoadTextView.setTextAppearance(getContext(), this.mTextAppearance);
        }
        setLoadingText(this.mLoadText);
        addView(inflate, layoutParams);
        startLoading(200);
    }

    private void startLoading(long j) {
        if (this.mAnimatorSet == null || !this.mAnimatorSet.isRunning()) {
            removeCallbacks(this.mFreeFallRunnable);
            if (j > 0) {
                postDelayed(this.mFreeFallRunnable, j);
            } else {
                post(this.mFreeFallRunnable);
            }
        }
    }

    private void stopLoading() {
        if (this.mAnimatorSet != null) {
            if (this.mAnimatorSet.isRunning()) {
                this.mAnimatorSet.cancel();
            }
            this.mAnimatorSet = null;
        }
        removeCallbacks(this.mFreeFallRunnable);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0) {
            startLoading(200);
        } else {
            stopLoading();
        }
    }

    public void setLoadingText(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mLoadTextView.setVisibility(8);
        } else {
            this.mLoadTextView.setVisibility(0);
        }
        this.mLoadTextView.setText(charSequence);
    }

    public void upThrow() {
        ObjectAnimator objectAnimator;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mRxShapeView, "translationY", new float[]{mDistance, 0.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mIndicationIm, "scaleX", new float[]{0.2f, 1.0f});
        switch (this.mRxShapeView.getShape()) {
            case SHAPE_RECT:
                objectAnimator = ObjectAnimator.ofFloat(this.mRxShapeView, "rotation", new float[]{0.0f, -120.0f});
                break;
            case SHAPE_CIRCLE:
                objectAnimator = ObjectAnimator.ofFloat(this.mRxShapeView, "rotation", new float[]{0.0f, 180.0f});
                break;
            case SHAPE_TRIANGLE:
                objectAnimator = ObjectAnimator.ofFloat(this.mRxShapeView, "rotation", new float[]{0.0f, 180.0f});
                break;
            default:
                objectAnimator = null;
                break;
        }
        ofFloat.setDuration(250);
        objectAnimator.setDuration(250);
        ofFloat.setInterpolator(new DecelerateInterpolator(this.factor));
        objectAnimator.setInterpolator(new DecelerateInterpolator(this.factor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(250);
        animatorSet.playTogether(new Animator[]{ofFloat, objectAnimator, ofFloat2});
        animatorSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                RxShapeLoadingView.this.freeFall();
            }
        });
        animatorSet.start();
    }

    public void freeFall() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mRxShapeView, "translationY", new float[]{0.0f, mDistance});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mIndicationIm, "scaleX", new float[]{1.0f, 0.2f});
        ofFloat.setDuration(250);
        ofFloat.setInterpolator(new AccelerateInterpolator(this.factor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(250);
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                RxShapeLoadingView.this.mRxShapeView.changeShape();
                RxShapeLoadingView.this.upThrow();
            }
        });
        animatorSet.start();
    }
}
