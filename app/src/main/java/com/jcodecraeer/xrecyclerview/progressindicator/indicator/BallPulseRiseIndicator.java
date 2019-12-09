package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.List;

public class BallPulseRiseIndicator extends BaseIndicatorController {
    public void draw(Canvas canvas, Paint paint) {
        float width = (float) (getWidth() / 10);
        float f = 2.0f * width;
        canvas.drawCircle((float) (getWidth() / 4), f, width, paint);
        canvas.drawCircle((float) ((getWidth() * 3) / 4), f, width, paint);
        canvas.drawCircle(width, ((float) getHeight()) - f, width, paint);
        canvas.drawCircle((float) (getWidth() / 2), ((float) getHeight()) - f, width, paint);
        canvas.drawCircle(((float) getWidth()) - width, ((float) getHeight()) - f, width, paint);
    }

    public List<Animator> createAnimation() {
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("rotationX", new float[]{0.0f, 360.0f});
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(getTarget(), new PropertyValuesHolder[]{ofFloat});
        ofPropertyValuesHolder.setInterpolator(new LinearInterpolator());
        ofPropertyValuesHolder.setRepeatCount(-1);
        ofPropertyValuesHolder.setDuration(1500);
        ofPropertyValuesHolder.start();
        ArrayList arrayList = new ArrayList();
        arrayList.add(ofPropertyValuesHolder);
        return arrayList;
    }
}
