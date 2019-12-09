package com.carlos.voiceline.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.android.hoinnet.highde.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VoiceLineView extends View {
    private final int LINE = 0;
    private final int RECT = 1;
    private float amplitude = 1.0f;
    private int fineness = 1;
    private boolean isSet = false;
    private long lastTime = 0;
    private int lineSpeed = 90;
    private float maxVolume = 100.0f;
    private int middleLineColor = ViewCompat.MEASURED_STATE_MASK;
    private float middleLineHeight = 4.0f;
    private int mode;
    private Paint paint;
    private Paint paintVoicLine;
    List<Path> paths = null;
    private float rectInitHeight = 4.0f;
    private List<Rect> rectList;
    private float rectSpace = 5.0f;
    private float rectWidth = 25.0f;
    private int sensibility = 4;
    private long speedY = 50;
    private float targetVolume = 1.0f;
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
        this.mode = obtainStyledAttributes.getInt(R.styleable.voiceView_viewMode, 0);
        this.voiceLineColor = obtainStyledAttributes.getColor(R.styleable.voiceView_voiceLine, ViewCompat.MEASURED_STATE_MASK);
        this.maxVolume = obtainStyledAttributes.getFloat(R.styleable.voiceView_maxVolume, 100.0f);
        this.sensibility = obtainStyledAttributes.getInt(R.styleable.voiceView_sensibility, 4);
        if (this.mode == 1) {
            this.rectWidth = obtainStyledAttributes.getDimension(R.styleable.voiceView_rectWidth, 25.0f);
            this.rectSpace = obtainStyledAttributes.getDimension(R.styleable.voiceView_rectSpace, 5.0f);
            this.rectInitHeight = obtainStyledAttributes.getDimension(R.styleable.voiceView_rectInitHeight, 4.0f);
        } else {
            this.middleLineColor = obtainStyledAttributes.getColor(R.styleable.voiceView_middleLine, ViewCompat.MEASURED_STATE_MASK);
            this.middleLineHeight = obtainStyledAttributes.getDimension(R.styleable.voiceView_middleLineHeight, 4.0f);
            this.lineSpeed = obtainStyledAttributes.getInt(R.styleable.voiceView_lineSpeed, 90);
            this.fineness = obtainStyledAttributes.getInt(R.styleable.voiceView_fineness, 1);
            this.paths = new ArrayList(20);
            for (int i = 0; i < 20; i++) {
                this.paths.add(new Path());
            }
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mode == 1) {
            drawVoiceRect(canvas);
        } else {
            drawMiddleLine(canvas);
            drawVoiceLine(canvas);
        }
        run();
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

    private void drawVoiceLine(Canvas canvas) {
        lineChange();
        if (this.paintVoicLine == null) {
            this.paintVoicLine = new Paint();
            this.paintVoicLine.setColor(this.voiceLineColor);
            this.paintVoicLine.setAntiAlias(true);
            this.paintVoicLine.setStyle(Paint.Style.STROKE);
            this.paintVoicLine.setStrokeWidth(2.0f);
        }
        canvas.save();
        int height = getHeight() / 2;
        for (int i = 0; i < this.paths.size(); i++) {
            this.paths.get(i).reset();
            this.paths.get(i).moveTo(0.0f, (float) (getHeight() / 2));
        }
        float width = (float) (getWidth() - 1);
        while (width >= 0.0f) {
            this.amplitude = (((this.volume * 4.0f) * width) / ((float) getWidth())) - (((((4.0f * this.volume) * width) * width) / ((float) getWidth())) / ((float) getWidth()));
            for (int i2 = 1; i2 <= this.paths.size(); i2++) {
                float sin = this.amplitude * ((float) Math.sin((((((double) width) - Math.pow(1.22d, (double) i2)) * 3.141592653589793d) / 180.0d) - ((double) this.translateX)));
                this.paths.get(i2 - 1).lineTo(width, (((((float) (2 * i2)) * sin) / ((float) this.paths.size())) - ((15.0f * sin) / ((float) this.paths.size()))) + ((float) height));
            }
            width -= (float) this.fineness;
        }
        for (int i3 = 0; i3 < this.paths.size(); i3++) {
            if (i3 == this.paths.size() - 1) {
                this.paintVoicLine.setAlpha(255);
            } else {
                this.paintVoicLine.setAlpha((i3 * 130) / this.paths.size());
            }
            if (this.paintVoicLine.getAlpha() > 0) {
                canvas.drawPath(this.paths.get(i3), this.paintVoicLine);
            }
        }
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

    public void setVolume(int i) {
        if (((float) i) > (this.maxVolume * ((float) this.sensibility)) / 25.0f) {
            this.isSet = true;
            this.targetVolume = ((float) ((getHeight() * i) / 2)) / this.maxVolume;
        }
    }

    private void lineChange() {
        if (this.lastTime == 0) {
            this.lastTime = System.currentTimeMillis();
            this.translateX = (float) (((double) this.translateX) + 1.5d);
        } else if (System.currentTimeMillis() - this.lastTime > ((long) this.lineSpeed)) {
            this.lastTime = System.currentTimeMillis();
            this.translateX = (float) (((double) this.translateX) + 1.5d);
        } else {
            return;
        }
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
        if (this.mode == 1) {
            postInvalidateDelayed(30);
        } else {
            invalidate();
        }
    }
}
