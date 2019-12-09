package com.android.hoinnet.highde.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import com.android.hoinnet.highde.R;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VoiceLineView extends View implements Runnable {
    private final int LINE = 0;
    private final int RECT = 1;
    private float amplitude = 1.0f;
    private boolean isSet = false;
    private boolean isViewAlive = true;
    private float maxVolume = 10.0f;
    private int middleLineColor = ViewCompat.MEASURED_STATE_MASK;
    private float middleLineHeight = 4.0f;
    private int mode;
    private Paint paint;
    private Paint paintVoicLine;
    List<Path> paths = null;
    private float rectInitHeight = 4.0f;
    private List<Rect> rectList;
    private float rectSpace = 5.0f;
    private float rectWidth = 5.0f;
    private int sensibility = 4;
    private long speedY = 5;
    private float targetVolume = 1.0f;
    private Thread thread;
    private float translateX = 0.0f;
    private int voiceLineColor = ViewCompat.MEASURED_STATE_MASK;
    private float volume = 10.0f;

    public VoiceLineView(Context context) {
        super(context);
    }

    public VoiceLineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initAtts(context, attributeSet);
    }

    public VoiceLineView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initAtts(context, attributeSet);
    }

    private void initAtts(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.voiceView);
        this.mode = obtainStyledAttributes.getInt(9, 0);
        this.voiceLineColor = obtainStyledAttributes.getColor(10, ViewCompat.MEASURED_STATE_MASK);
        this.maxVolume = obtainStyledAttributes.getFloat(2, 100.0f);
        this.sensibility = obtainStyledAttributes.getInt(8, 4);
        if (this.mode == 1) {
            this.rectWidth = obtainStyledAttributes.getDimension(7, 25.0f);
            this.rectSpace = obtainStyledAttributes.getDimension(6, 5.0f);
            this.rectInitHeight = obtainStyledAttributes.getDimension(5, 4.0f);
        } else {
            this.middleLineColor = obtainStyledAttributes.getColor(3, ViewCompat.MEASURED_STATE_MASK);
            this.middleLineHeight = obtainStyledAttributes.getDimension(4, 4.0f);
            this.paths = new ArrayList();
            for (int i = 0; i < 10; i++) {
                this.paths.add(new Path());
            }
        }
        obtainStyledAttributes.recycle();
        this.thread = new Thread(this);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mode == 1) {
            drawVoiceRect(canvas);
            return;
        }
        drawMiddleLine(canvas);
        drawVoiceLine(canvas);
    }

    private void drawMiddleLine(Canvas canvas) {
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(this.middleLineColor);
            this.paint.setAntiAlias(true);
        }
        canvas.save();
        canvas.drawRect(0.0f, ((float) (getHeight() / 2)) - (this.middleLineHeight / 2.0f), (float) getWidth(), ((float) (getHeight() / 2)) + (this.middleLineHeight / 2.0f), this.paint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.isViewAlive = false;
        super.onDetachedFromWindow();
    }

    private void drawVoiceLine(Canvas canvas) {
        lineChange();
        float f = 1.0f;
        int i = 1;
        if (this.paintVoicLine == null) {
            this.paintVoicLine = new Paint();
            this.paintVoicLine.setColor(this.voiceLineColor);
            this.paintVoicLine.setAntiAlias(true);
            this.paintVoicLine.setStyle(Paint.Style.STROKE);
            this.paintVoicLine.setStrokeWidth(1.0f);
        }
        canvas.save();
        int height = getHeight() / 2;
        for (int i2 = 0; i2 < this.paths.size(); i2++) {
            this.paths.get(i2).reset();
            this.paths.get(i2).moveTo(0.0f, (float) (getHeight() / 2));
        }
        float width = (float) (getWidth() - 1);
        while (width >= 0.0f) {
            this.amplitude = (((this.volume * 4.0f) * width) / ((float) getWidth())) - (((((4.0f * this.volume) * width) * width) / ((float) getWidth())) / ((float) getWidth()));
            int i3 = i;
            while (i3 <= this.paths.size()) {
                double sin = ((double) this.amplitude) * Math.sin((((((double) width) - Math.pow(1.22d, (double) i3)) * 3.141592653589793d) / 180.0d) - ((double) this.translateX));
                this.paths.get(i3 - 1).lineTo(width, (float) ((((((double) (2 * i3)) * sin) / ((double) this.paths.size())) - ((15.0d * sin) / ((double) this.paths.size()))) + ((double) height)));
                i3++;
                f = 1.0f;
            }
            width -= f;
            i = 1;
        }
        for (int i4 = 0; i4 < this.paths.size(); i4++) {
            if (i4 == this.paths.size() - 1) {
                this.paintVoicLine.setAlpha(255);
            } else {
                this.paintVoicLine.setAlpha((i4 * TransportMediator.KEYCODE_MEDIA_RECORD) / this.paths.size());
            }
            if (this.paintVoicLine.getAlpha() > 0) {
                canvas.drawPath(this.paths.get(i4), this.paintVoicLine);
            } else {
                Canvas canvas2 = canvas;
            }
        }
        Canvas canvas3 = canvas;
        canvas.restore();
    }

    private void drawVoiceRect(Canvas canvas) {
        if (this.paintVoicLine == null) {
            this.paintVoicLine = new Paint();
            this.paintVoicLine.setColor(this.voiceLineColor);
            this.paintVoicLine.setAntiAlias(true);
            this.paintVoicLine.setStyle(Paint.Style.FILL);
            this.paintVoicLine.setStrokeWidth(2.0f);
        }
        if (this.rectList == null) {
            this.rectList = new LinkedList();
        }
        long j = (long) ((int) (this.rectSpace + this.rectWidth));
        if (this.speedY % j < 6) {
            Rect rect = new Rect((int) ((((-this.rectWidth) - 10.0f) - ((float) this.speedY)) + ((float) (this.speedY % j))), (int) ((((float) (getHeight() / 2)) - (this.rectInitHeight / 2.0f)) - (this.volume == 10.0f ? 0.0f : this.volume / 2.0f)), (int) ((-10 - this.speedY) + (this.speedY % j)), (int) (((float) (getHeight() / 2)) + (this.rectInitHeight / 2.0f) + (this.volume == 10.0f ? 0.0f : this.volume / 2.0f)));
            if (((float) this.rectList.size()) > (((float) getWidth()) / (this.rectSpace + this.rectWidth)) + 2.0f) {
                this.rectList.remove(0);
            }
            this.rectList.add(rect);
        }
        canvas.translate((float) this.speedY, 0.0f);
        for (int size = this.rectList.size() - 1; size >= 0; size--) {
            canvas.drawRect(this.rectList.get(size), this.paintVoicLine);
        }
        rectChange();
    }

    public void start() {
        if (this.thread != null) {
            this.thread.start();
        }
    }

    public void setVolume(int i) {
        if (((float) i) > (this.maxVolume * ((float) this.sensibility)) / 25.0f) {
            this.isSet = true;
            this.targetVolume = ((float) ((getHeight() * i) / 2)) / this.maxVolume;
        }
    }

    private void lineChange() {
        this.translateX += 1.0f;
        if (this.volume >= this.targetVolume || !this.isSet) {
            this.isSet = false;
            if (this.volume <= 10.0f) {
                this.volume = 10.0f;
            } else if (this.volume < ((float) (getHeight() / 30))) {
                this.volume -= (float) (getHeight() / 60);
            } else {
                this.volume -= (float) (getHeight() / 30);
            }
        } else {
            this.volume += (float) (getHeight() / 30);
        }
    }

    private void rectChange() {
        this.speedY += 6;
        if (this.volume >= this.targetVolume || !this.isSet) {
            this.isSet = false;
            if (this.volume <= 10.0f) {
                this.volume = 10.0f;
            } else if (this.volume < ((float) (getHeight() / 30))) {
                this.volume -= (float) (getHeight() / 60);
            } else {
                this.volume -= (float) (getHeight() / 30);
            }
        } else {
            this.volume += (float) (getHeight() / 30);
        }
    }

    public void run() {
        while (this.isViewAlive) {
            if (this.mode == 1) {
                postInvalidate();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                postInvalidate();
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
