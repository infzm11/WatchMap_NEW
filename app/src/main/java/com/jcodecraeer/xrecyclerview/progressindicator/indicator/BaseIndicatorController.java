package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import java.util.List;

public abstract class BaseIndicatorController {
    private List<Animator> mAnimators;
    private View mTarget;

    public enum AnimStatus {
        START,
        END,
        CANCEL
    }

    public abstract List<Animator> createAnimation();

    public abstract void draw(Canvas canvas, Paint paint);

    public void setTarget(View view) {
        this.mTarget = view;
    }

    public View getTarget() {
        return this.mTarget;
    }

    public int getWidth() {
        return this.mTarget.getWidth();
    }

    public int getHeight() {
        return this.mTarget.getHeight();
    }

    public void postInvalidate() {
        this.mTarget.postInvalidate();
    }

    public void initAnimation() {
        this.mAnimators = createAnimation();
    }

    public void setAnimationStatus(AnimStatus animStatus) {
        if (this.mAnimators != null) {
            int size = this.mAnimators.size();
            for (int i = 0; i < size; i++) {
                Animator animator = this.mAnimators.get(i);
                boolean isRunning = animator.isRunning();
                switch (animStatus) {
                    case START:
                        if (isRunning) {
                            break;
                        } else {
                            animator.start();
                            break;
                        }
                    case END:
                        if (!isRunning) {
                            break;
                        } else {
                            animator.end();
                            break;
                        }
                    case CANCEL:
                        if (!isRunning) {
                            break;
                        } else {
                            animator.cancel();
                            break;
                        }
                }
            }
        }
    }
}
