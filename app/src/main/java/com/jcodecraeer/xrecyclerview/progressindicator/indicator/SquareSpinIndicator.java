package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.List;

public class SquareSpinIndicator extends BaseIndicatorController {
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(new RectF((float) (getWidth() / 5), (float) (getHeight() / 5), (float) ((getWidth() * 4) / 5), (float) ((getHeight() * 4) / 5)), paint);
    }

    public List<Animator> createAnimation() {
        ArrayList arrayList = new ArrayList();
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("rotationX", new float[]{0.0f, 180.0f, 180.0f, 0.0f, 0.0f});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("rotationY", new float[]{0.0f, 0.0f, 180.0f, 180.0f, 0.0f});
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(getTarget(), new PropertyValuesHolder[]{ofFloat2, ofFloat});
        ofPropertyValuesHolder.setInterpolator(new LinearInterpolator());
        ofPropertyValuesHolder.setRepeatCount(-1);
        ofPropertyValuesHolder.setDuration(2500);
        ofPropertyValuesHolder.start();
        arrayList.add(ofPropertyValuesHolder);
        return arrayList;
    }
}
