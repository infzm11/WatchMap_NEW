package com.android.hoinnet.highde.view.popView;

import java.util.Locale;

class RxPopupViewTool {
    RxPopupViewTool() {
    }

    static boolean isRtl() {
        Locale locale = Locale.getDefault();
        return Character.getDirectionality(locale.getDisplayName(locale).charAt(0)) == 1;
    }
}
