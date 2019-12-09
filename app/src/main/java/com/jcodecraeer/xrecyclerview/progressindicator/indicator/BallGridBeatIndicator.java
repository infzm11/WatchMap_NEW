package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.amap.api.services.core.AMapException;
import java.util.ArrayList;
import java.util.List;

public class BallGridBeatIndicator extends BaseIndicatorController {
    public static final int ALPHA = 255;
    int[] alphas = {255, 255, 255, 255, 255, 255, 255, 255, 255};

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
                paint.setAlpha(this.alphas[(3 * i) + i2]);
                canvas.drawCircle(0.0f, 0.0f, width, paint);
                canvas.restore();
            }
        }
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        int[] iArr = {960, 930, 1190, 1130, 1340, 940, AMapException.CODE_AMAP_SERVICE_INVALID_PARAMS, 820, 1190};
        int[] iArr2 = {360, 400, 680, 410, 710, -150, -120, 10, 320};
        for (final int i = 0; i < 9; i++) {
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{255, 168, 255});
            ofInt.setDuration((long) iArr[i]);
            ofInt.setRepeatCount(-1);
            ofInt.setStartDelay((long) iArr2[i]);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BallGridBeatIndicator.this.alphas[i] = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    BallGridBeatIndicator.this.postInvalidate();
                }
            });
            ofInt.start();
            arrayList.add(ofInt);
        }
        return arrayList;
    }
}
