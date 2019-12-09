package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;
import com.android.hoinnet.highde.R;

public class LoadingMoreFooter extends LinearLayout {
    public static final int STATE_COMPLETE = 1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_NOMORE = 2;
    private TextView mText;
    private SimpleViewSwitcher progressCon;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    public LoadingMoreFooter(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public void initView() {
        setGravity(17);
        setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        this.progressCon = new SimpleViewSwitcher(getContext());
        this.progressCon.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        AVLoadingIndicatorView aVLoadingIndicatorView = new AVLoadingIndicatorView(getContext());
        aVLoadingIndicatorView.setIndicatorColor(-4868683);
        aVLoadingIndicatorView.setIndicatorId(22);
        this.progressCon.setView(aVLoadingIndicatorView);
        addView(this.progressCon);
        this.mText = new TextView(getContext());
        this.mText.setText("正在加载...");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);
        this.mText.setLayoutParams(layoutParams);
        addView(this.mText);
    }

    public void setProgressStyle(int i) {
        if (i == -1) {
            this.progressCon.setView(new ProgressBar(getContext(), (AttributeSet) null, 16842871));
            return;
        }
        AVLoadingIndicatorView aVLoadingIndicatorView = new AVLoadingIndicatorView(getContext());
        aVLoadingIndicatorView.setIndicatorColor(-4868683);
        aVLoadingIndicatorView.setIndicatorId(i);
        this.progressCon.setView(aVLoadingIndicatorView);
    }

    public void setState(int i) {
        switch (i) {
            case 0:
                this.progressCon.setVisibility(VISIBLE);
                this.mText.setText(getContext().getText(R.string.listview_loading));
                setVisibility(VISIBLE);
                return;
            case 1:
                this.mText.setText(getContext().getText(R.string.listview_loading));
                setVisibility(GONE);
                return;
            case 2:
                this.mText.setText(getContext().getText(R.string.nomore_loading));
                this.progressCon.setVisibility(GONE);
                setVisibility(VISIBLE);
                return;
            default:
                return;
        }
    }
}
