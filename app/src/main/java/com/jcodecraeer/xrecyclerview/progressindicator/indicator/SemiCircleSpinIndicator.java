package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;

public class SemiCircleSpinIndicator extends BaseIndicatorController {
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawArc(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()), -60.0f, 120.0f, false, paint);
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(getTarget(), "rotation", new float[]{0.0f, 180.0f, 360.0f});
        ofFloat.setDuration(600);
        ofFloat.setRepeatCount(-1);
        ofFloat.start();
        arrayList.add(ofFloat);
        return arrayList;
    }
}
