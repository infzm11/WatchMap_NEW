package com.android.hoinnet.highde.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.hoinnet.highde.R;

public class RxDialogSureCancel extends RxDialog {
    private ImageView mIvLogo;
    private TextView mTvCancel;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvTitle;

    public RxDialogSureCancel(Context context, int i) {
        super(context, i);
        initView();
    }

    public RxDialogSureCancel(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        initView();
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Activity activity) {
        super(activity);
        initView();
    }

    public RxDialogSureCancel(Context context, float f, int i) {
        super(context, f, i);
        initView();
    }

    public ImageView getLogoView() {
        return this.mIvLogo;
    }

    public void setTitle(String str) {
        this.mTvTitle.setText(str);
    }

    public TextView getTitleView() {
        return this.mTvTitle;
    }

    public void setContent(String str) {
        this.mTvContent.setText(str);
    }

    public TextView getContentView() {
        return this.mTvContent;
    }

    public void setSure(String str) {
        this.mTvSure.setText(str);
    }

    public TextView getSureView() {
        return this.mTvSure;
    }

    public void setCancel(String str) {
        this.mTvCancel.setText(str);
    }

    public TextView getCancelView() {
        return this.mTvCancel;
    }

    public void setSureListener(View.OnClickListener onClickListener) {
        this.mTvSure.setOnClickListener(onClickListener);
    }

    public void setCancelListener(View.OnClickListener onClickListener) {
        this.mTvCancel.setOnClickListener(onClickListener);
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, (ViewGroup) null);
        this.mIvLogo = (ImageView) inflate.findViewById(R.id.iv_logo);
        this.mTvSure = (TextView) inflate.findViewById(R.id.tv_sure);
        this.mTvCancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        this.mTvContent = (TextView) inflate.findViewById(R.id.tv_content);
        this.mTvContent.setTextIsSelectable(true);
        this.mTvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        setContentView(inflate);
    }
}
