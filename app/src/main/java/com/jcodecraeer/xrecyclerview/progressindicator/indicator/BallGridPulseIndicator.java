package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.view.PointerIconCompat;
import java.util.ArrayList;
import java.util.List;

public class BallGridPulseIndicator extends BaseIndicatorController {
    public static final int ALPHA = 255;
    public static final float SCALE = 1.0f;
    int[] alphas = {255, 255, 255, 255, 255, 255, 255, 255, 255};
    float[] scaleFloats = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    public void draw(Canvas canvas, Paint paint) {
        float width = (((float) getWidth()) - 16.0f) / 6.0f;
        float f = 2.0f * width;
        float f2 = f + 4.0f;
        float width2 = ((float) (getWidth() / 2)) - f2;
        float width3 = ((float) (getWidth() / 2)) - f2;
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                canvas.save();
                float f3 = (float) i2;
                float f4 = (f * f3) + width2 + (f3 * 4.0f);
                float f5 = (float) i;
                canvas.translate(f4, (f * f5) + width3 + (f5 * 4.0f));
                int i3 = (3 * i) + i2;
                canvas.scale(this.scaleFloats[i3], this.scaleFloats[i3]);
                paint.setAlpha(this.alphas[i3]);
                canvas.drawCircle(0.0f, 0.0f, width, paint);
                canvas.restore();
            }
        }
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = {720, PointerIconCompat.TYPE_GRAB, 1280, 1420, 1450, 1180, 870, 1450, 1060};
        int[] iArr2 = {-60, 250, -170, 480, 310, 30, 460, 780, 450};
        for (int i = 0; i < 9; i++) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.5f, 1.0f});
            ofFloat.setDuration((long) iArr[i]);
            ofFloat.setRepeatCount(-1);
            ofFloat.setStartDelay((long) iArr2[i]);
            final int finalI = i;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BallGridPulseIndicator.this.scaleFloats[finalI] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    BallGridPulseIndicator.this.postInvalidate();
                }
            });
            ofFloat.start();
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{255, 210, 122, 255});
            ofInt.setDuration((long) iArr[i]);
            ofInt.setRepeatCount(-1);
            ofInt.setStartDelay((long) iArr2[i]);
            final int finalI1 = i;
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BallGridPulseIndicator.this.alphas[finalI1] = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    BallGridPulseIndicator.this.postInvalidate();
                }
            });
            ofInt.start();
            arrayList.add(ofFloat);
            arrayList.add(ofInt);
        }
        return arrayList;
    }
}
