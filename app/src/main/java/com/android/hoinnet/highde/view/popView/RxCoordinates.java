package com.android.hoinnet.highde.view.popView;

import android.view.View;

public class RxCoordinates {
    int bottom;
    int left;
    int right;
    int top;

    public RxCoordinates(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.left = iArr[0];
        this.right = this.left + view.getWidth();
        this.top = iArr[1];
        this.bottom = this.top + view.getHeight();
    }
}
