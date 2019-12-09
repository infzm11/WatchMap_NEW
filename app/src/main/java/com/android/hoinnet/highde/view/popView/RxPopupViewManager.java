package com.android.hoinnet.highde.view.popView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.graphics.Point;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.android.hoinnet.highde.utils.RxAnimationTool;

import java.util.HashMap;
import java.util.Map;

public class RxPopupViewManager {
    private static final int DEFAULT_ANIM_DURATION = 400;
    private static final String TAG = "RxPopupViewManager";
    private int mAnimationDuration = DEFAULT_ANIM_DURATION;
    /* access modifiers changed from: private */
    public TipListener mListener;
    private Map<Integer, View> mTipsMap = new HashMap();

    public interface TipListener {
        void onTipDismissed(View view, int i, boolean z);
    }

    public RxPopupViewManager() {
    }

    public RxPopupViewManager(TipListener tipListener) {
        this.mListener = tipListener;
    }

    public View show(RxPopupView rxPopupView) {
        View create = create(rxPopupView);
        if (create == null) {
            return null;
        }
        RxAnimationTool.popup(create, (long) this.mAnimationDuration).start();
        return create;
    }

    private View create(RxPopupView rxPopupView) {
        if (rxPopupView.getAnchorView() == null) {
            Log.e(TAG, "Unable to create a tip, anchor view is null");
            return null;
        } else if (rxPopupView.getRootView() == null) {
            Log.e(TAG, "Unable to create a tip, root layout is null");
            return null;
        } else if (this.mTipsMap.containsKey(Integer.valueOf(rxPopupView.getAnchorView().getId()))) {
            return this.mTipsMap.get(Integer.valueOf(rxPopupView.getAnchorView().getId()));
        } else {
            TextView createTipView = createTipView(rxPopupView);
            if (RxPopupViewTool.isRtl()) {
                switchToolTipSidePosition(rxPopupView);
            }
            RxPopupViewBackgroundConstructor.setBackground(createTipView, rxPopupView);
            rxPopupView.getRootView().addView(createTipView);
            moveTipToCorrectPosition(createTipView, RxPopupViewCoordinatesFinder.getCoordinates(createTipView, rxPopupView));
            createTipView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RxPopupViewManager.this.dismiss(view, true);
                }
            });
            int id = rxPopupView.getAnchorView().getId();
            createTipView.setTag(Integer.valueOf(id));
            this.mTipsMap.put(Integer.valueOf(id), createTipView);
            return createTipView;
        }
    }

    private void moveTipToCorrectPosition(TextView textView, Point point) {
        RxCoordinates rxCoordinates = new RxCoordinates(textView);
        int i = point.x - rxCoordinates.left;
        int i2 = point.y - rxCoordinates.top;
        if (!RxPopupViewTool.isRtl()) {
            textView.setTranslationX((float) i);
        } else {
            textView.setTranslationX((float) (-i));
        }
        textView.setTranslationY((float) i2);
    }

    @NonNull
    private TextView createTipView(RxPopupView rxPopupView) {
        TextView textView = new TextView(rxPopupView.getContext());
        textView.setTextColor(rxPopupView.getTextColor());
        textView.setTextSize((float) rxPopupView.getTextSize());
        textView.setText(rxPopupView.getMessage() != null ? rxPopupView.getMessage() : rxPopupView.getSpannableMessage());
        textView.setVisibility(View.INVISIBLE);
        textView.setGravity(rxPopupView.getTextGravity());
        setTipViewElevation(textView, rxPopupView);
        return textView;
    }

    private void setTipViewElevation(TextView textView, RxPopupView rxPopupView) {
        if (Build.VERSION.SDK_INT >= 21 && rxPopupView.getElevation() > 0.0f) {
            textView.setOutlineProvider(new ViewOutlineProvider() {
                @SuppressLint({"NewApi"})
                public void getOutline(View view, Outline outline) {
                    outline.setEmpty();
                }
            });
            textView.setElevation(rxPopupView.getElevation());
        }
    }

    private void switchToolTipSidePosition(RxPopupView rxPopupView) {
        if (rxPopupView.positionedLeftTo()) {
            rxPopupView.setPosition(4);
        } else if (rxPopupView.positionedRightTo()) {
            rxPopupView.setPosition(3);
        }
    }

    public void setAnimationDuration(int i) {
        this.mAnimationDuration = i;
    }

    public boolean dismiss(View view, boolean z) {
        if (view == null || !isVisible(view)) {
            return false;
        }
        this.mTipsMap.remove(Integer.valueOf(((Integer) view.getTag()).intValue()));
        animateDismiss(view, z);
        return true;
    }

    public boolean dismiss(Integer num) {
        return this.mTipsMap.containsKey(num) && dismiss(this.mTipsMap.get(num), false);
    }

    public View find(Integer num) {
        if (this.mTipsMap.containsKey(num)) {
            return this.mTipsMap.get(num);
        }
        return null;
    }

    public boolean findAndDismiss(View view) {
        View find = find(Integer.valueOf(view.getId()));
        if (find == null || !dismiss(find, false)) {
            return false;
        }
        return true;
    }

    public void clear() {
        if (!this.mTipsMap.isEmpty()) {
            for (Map.Entry<Integer, View> value : this.mTipsMap.entrySet()) {
                dismiss((View) value.getValue(), false);
            }
        }
        this.mTipsMap.clear();
    }

    private void animateDismiss(final View view, final boolean z) {
        RxAnimationTool.popout(view, (long) this.mAnimationDuration, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (RxPopupViewManager.this.mListener != null) {
                    RxPopupViewManager.this.mListener.onTipDismissed(view, ((Integer) view.getTag()).intValue(), z);
                }
            }
        }).start();
    }

    public boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }
}
