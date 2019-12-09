package com.android.hoinnet.highde.view.popView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import com.android.hoinnet.highde.R;

class RxPopupViewBackgroundConstructor {
    RxPopupViewBackgroundConstructor() {
    }

    static void setBackground(View view, RxPopupView rxPopupView) {
        if (rxPopupView.hideArrow()) {
            setToolTipNoArrowBackground(view, rxPopupView.getBackgroundColor());
            return;
        }
        switch (rxPopupView.getPosition()) {
            case 0:
                setToolTipAboveBackground(view, rxPopupView);
                return;
            case 1:
                setToolTipBelowBackground(view, rxPopupView);
                return;
            case 3:
                setToolTipLeftToBackground(view, rxPopupView.getBackgroundColor());
                return;
            case 4:
                setToolTipRightToBackground(view, rxPopupView.getBackgroundColor());
                return;
            default:
                return;
        }
    }

    private static void setToolTipAboveBackground(View view, RxPopupView rxPopupView) {
        int align = rxPopupView.getAlign();
        int i = R.mipmap.tooltip_arrow_down_left;
        switch (align) {
            case 0:
                setTipBackground(view, R.mipmap.tooltip_arrow_down, rxPopupView.getBackgroundColor());
                return;
            case 1:
                if (RxPopupViewTool.isRtl()) {
                    i = R.mipmap.tooltip_arrow_down_right;
                }
                setTipBackground(view, i, rxPopupView.getBackgroundColor());
                return;
            case 2:
                if (!RxPopupViewTool.isRtl()) {
                    i = R.mipmap.tooltip_arrow_down_right;
                }
                setTipBackground(view, i, rxPopupView.getBackgroundColor());
                return;
            default:
                return;
        }
    }

    private static void setToolTipBelowBackground(View view, RxPopupView rxPopupView) {
        int align = rxPopupView.getAlign();
        int i = R.mipmap.tooltip_arrow_up_left;
        switch (align) {
            case 0:
                setTipBackground(view, R.mipmap.tooltip_arrow_up, rxPopupView.getBackgroundColor());
                return;
            case 1:
                if (RxPopupViewTool.isRtl()) {
                    i = R.mipmap.tooltip_arrow_up_right;
                }
                setTipBackground(view, i, rxPopupView.getBackgroundColor());
                return;
            case 2:
                if (!RxPopupViewTool.isRtl()) {
                    i = R.mipmap.tooltip_arrow_up_right;
                }
                setTipBackground(view, i, rxPopupView.getBackgroundColor());
                return;
            default:
                return;
        }
    }

    private static void setToolTipLeftToBackground(View view, int i) {
        setTipBackground(view, !RxPopupViewTool.isRtl() ? R.mipmap.tooltip_arrow_right : R.mipmap.tooltip_arrow_left, i);
    }

    private static void setToolTipRightToBackground(View view, int i) {
        setTipBackground(view, !RxPopupViewTool.isRtl() ? R.mipmap.tooltip_arrow_left : R.mipmap.tooltip_arrow_right, i);
    }

    private static void setToolTipNoArrowBackground(View view, int i) {
        setTipBackground(view, R.mipmap.tooltip_no_arrow, i);
    }

    private static void setTipBackground(View view, int i, int i2) {
        setViewBackground(view, getTintedDrawable(view.getContext(), i, i2));
    }

    private static void setViewBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private static Drawable getTintedDrawable(Context context, int i, int i2) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = context.getResources().getDrawable(i, (Resources.Theme) null);
            if (drawable != null) {
                drawable.setTint(i2);
            }
        } else {
            drawable = context.getResources().getDrawable(i);
            if (drawable != null) {
                drawable.setColorFilter(i2, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return drawable;
    }
}
