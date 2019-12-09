package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.jcodecraeer.xrecyclerview.progressindicator.indicator.BallSpinFadeLoaderIndicator;

public class LineSpinFadeLoaderIndicator extends BallSpinFadeLoaderIndicator {
    public void draw(Canvas canvas, Paint paint) {
        float width = (float) (getWidth() / 10);
        for (int i = 0; i < 8; i++) {
            canvas.save();
            BallSpinFadeLoaderIndicator.Point circleAt = circleAt(getWidth(), getHeight(), (((float) getWidth()) / 2.5f) - width, 0.7853981633974483d * ((double) i));
            canvas.translate(circleAt.x, circleAt.y);
            canvas.scale(this.scaleFloats[i], this.scaleFloats[i]);
            canvas.rotate((float) (i * 45));
            paint.setAlpha(this.alphas[i]);
            float f = -width;
            canvas.drawRoundRect(new RectF(f, f / 1.5f, 1.5f * width, width / 1.5f), 5.0f, 5.0f, paint);
            canvas.restore();
        }
    }
}
