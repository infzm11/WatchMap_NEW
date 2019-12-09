package com.android.hoinnet.highde.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import com.android.hoinnet.highde.interfaces.OnUpdateListener;

public class RxAnimationTool {
    static ObjectAnimator invisToVis;
    static ObjectAnimator visToInvis;

    public static void animationColorGradient(int i, int i2, final OnUpdateListener onUpdateListener) {
        ValueAnimator duration = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}).setDuration(3000);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                onUpdateListener.onUpdate(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        duration.start();
    }

    public static void cardFilpAnimation(final View view, final View view2) {
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        if (view.getVisibility() == View.GONE) {
            invisToVis = ObjectAnimator.ofFloat(view, "rotationY", new float[]{-90.0f, 0.0f});
            visToInvis = ObjectAnimator.ofFloat(view2, "rotationY", new float[]{0.0f, 90.0f});
        } else if (view2.getVisibility() == View.GONE) {
            invisToVis = ObjectAnimator.ofFloat(view2, "rotationY", new float[]{-90.0f, 0.0f});
            visToInvis = ObjectAnimator.ofFloat(view, "rotationY", new float[]{0.0f, 90.0f});
        }
        visToInvis.setDuration(250);
        visToInvis.setInterpolator(accelerateInterpolator);
        invisToVis.setDuration(250);
        invisToVis.setInterpolator(decelerateInterpolator);
        visToInvis.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (view.getVisibility() == View.GONE) {
                    view2.setVisibility(View.GONE);
                    RxAnimationTool.invisToVis.start();
                    view.setVisibility(View.VISIBLE);
                    return;
                }
                view2.setVisibility(View.GONE);
                RxAnimationTool.visToInvis.start();
                view.setVisibility(View.VISIBLE);
            }
        });
        visToInvis.start();
    }

    public static void zoomIn(View view, float f, float f2) {
        view.setPivotY((float) view.getHeight());
        view.setPivotX((float) (view.getWidth() / 2));
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0f, f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0f, f});
        animatorSet.play(ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0f, -f2})).with(ofFloat);
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public static void zoomOut(View view, float f) {
        view.setPivotY((float) view.getHeight());
        view.setPivotX((float) (view.getWidth() / 2));
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "scaleX", new float[]{f, 1.0f});
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "scaleY", new float[]{f, 1.0f});
        animatorSet.play(ObjectAnimator.ofFloat(view, "translationY", new float[]{view.getTranslationY(), 0.0f})).with(ofFloat);
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public static void ScaleUpDowm(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        scaleAnimation.setRepeatCount(-1);
        scaleAnimation.setRepeatMode(1);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        scaleAnimation.setDuration(1200);
        view.startAnimation(scaleAnimation);
    }

    public static void animateHeight(int i, int i2, final View view) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = intValue;
                view.setLayoutParams(layoutParams);
            }
        });
        ofInt.start();
    }

    public static ObjectAnimator popup(View view, long j) {
        view.setAlpha(0.0f);
        view.setVisibility(View.VISIBLE);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.0f, 1.0f})});
        ofPropertyValuesHolder.setDuration(j);
        ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator());
        return ofPropertyValuesHolder;
    }

    public static ObjectAnimator popout(final View view, long j, final AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.0f})});
        ofPropertyValuesHolder.setDuration(j);
        ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                view.setVisibility(View.GONE);
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animator);
                }
            }
        });
        ofPropertyValuesHolder.setInterpolator(new AnticipateOvershootInterpolator());
        return ofPropertyValuesHolder;
    }
}
