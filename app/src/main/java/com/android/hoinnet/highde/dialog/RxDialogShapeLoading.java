package com.android.hoinnet.highde.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.hoinnet.highde.R;
import com.android.hoinnet.highde.view.RxShapeLoadingView;

public class RxDialogShapeLoading extends RxDialog {
    private View mDialogContentView;
    private RxShapeLoadingView mLoadingView;

    public enum RxCancelType {
        normal,
        error,
        success,
        info
    }

    public RxDialogShapeLoading(Context context, int i) {
        super(context, i);
        initView(context);
    }

    public RxDialogShapeLoading(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        initView(context);
    }

    public RxDialogShapeLoading(Context context) {
        super(context);
        initView(context);
    }

    public RxDialogShapeLoading(Activity activity) {
        super(activity);
        initView(activity);
    }

    public RxDialogShapeLoading(Context context, float f, int i) {
        super(context, f, i);
        initView(context);
    }

    private void initView(Context context) {
        this.mDialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_shape_loading_view, (ViewGroup) null);
        this.mLoadingView = (RxShapeLoadingView) this.mDialogContentView.findViewById(R.id.loadView);
        setContentView(this.mDialogContentView);
    }

    public void cancel(RxCancelType rxCancelType, String str) {
        cancel();
        switch (rxCancelType) {
            case normal:
                Toast.makeText(getContext(), str, 0).show();
                return;
            case error:
                Toast.makeText(getContext(), str, 0).show();
                return;
            case success:
                Toast.makeText(getContext(), str, 0).show();
                return;
            case info:
                Toast.makeText(getContext(), str, 0).show();
                return;
            default:
                Toast.makeText(getContext(), str, 0).show();
                return;
        }
    }

    public void cancel(String str) {
        cancel();
        Toast.makeText(getContext(), str, 0).show();
    }

    public void setLoadingText(CharSequence charSequence) {
        this.mLoadingView.setLoadingText(charSequence);
    }

    public RxShapeLoadingView getLoadingView() {
        return this.mLoadingView;
    }

    public View getDialogContentView() {
        return this.mDialogContentView;
    }
}
