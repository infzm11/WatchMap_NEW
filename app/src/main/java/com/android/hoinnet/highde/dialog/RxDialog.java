package com.android.hoinnet.highde.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import com.amap.api.services.core.AMapException;
import com.android.hoinnet.highde.R;

public class RxDialog extends Dialog {
    protected Context mContext;
    protected WindowManager.LayoutParams mLayoutParams;

    public WindowManager.LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public RxDialog(Context context, int i) {
        super(context, i);
        initView(context);
    }

    public RxDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        initView(context);
    }

    public RxDialog(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        requestWindowFeature(1);
        getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        this.mContext = context;
        Window window = getWindow();
        this.mLayoutParams = window.getAttributes();
        this.mLayoutParams.alpha = 1.0f;
        window.setAttributes(this.mLayoutParams);
        if (this.mLayoutParams != null) {
            this.mLayoutParams.height = -2;
            this.mLayoutParams.width = -2;
            this.mLayoutParams.gravity = 17;
        }
    }

    public RxDialog(Context context, float f, int i) {
        super(context);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        this.mContext = context;
        Window window = getWindow();
        this.mLayoutParams = window.getAttributes();
        this.mLayoutParams.alpha = 1.0f;
        window.setAttributes(this.mLayoutParams);
        if (this.mLayoutParams != null) {
            this.mLayoutParams.height = -1;
            this.mLayoutParams.gravity = i;
        }
    }

    public void skipTools() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().setFlags(1024, 1024);
        }
    }

    public void setFullScreen() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        window.setAttributes(attributes);
    }

    public void setFullScreenWidth() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -2;
        window.setAttributes(attributes);
    }

    public void setFullScreenHeight() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -2;
        attributes.height = -1;
        window.setAttributes(attributes);
    }

    public void setOnWhole() {
        getWindow().setType(AMapException.CODE_AMAP_ENGINE_TABLEID_NOT_EXIST);
    }
}
