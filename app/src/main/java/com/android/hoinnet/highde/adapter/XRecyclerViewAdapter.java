package com.android.hoinnet.highde.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amap.api.services.core.PoiItem;
import com.android.hoinnet.highde.R;
import java.util.ArrayList;
import java.util.List;

public class XRecyclerViewAdapter extends RecyclerView.Adapter<XRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "XRecyclerViewAdapter";
    private Context mContext;
    private List<PoiItem> mList;
    /* access modifiers changed from: private */
    public XRecyclerViewOnItemClick mXRecyclerViewOnItemClick;

    class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView mTextAddress;
        /* access modifiers changed from: private */
        public TextView mTextName;

        public ViewHolder(View view) {
            super(view);
            this.mTextName = (TextView) view.findViewById(R.id.name);
            this.mTextAddress = (TextView) view.findViewById(R.id.address);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (XRecyclerViewAdapter.this.mXRecyclerViewOnItemClick != null) {
                        XRecyclerViewAdapter.this.mXRecyclerViewOnItemClick.XRecyclerViewOnClickItem(view, ViewHolder.this.getLayoutPosition() - 1);
                    }
                }
            });
        }
    }

    public interface XRecyclerViewOnItemClick {
        void XRecyclerViewOnClickItem(View view, int i);
    }

    public XRecyclerViewAdapter(Context context, ArrayList<PoiItem> arrayList) {
        this.mContext = context;
        this.mList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.layout_list_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PoiItem poiItem = this.mList.get(i);
        viewHolder.mTextName.setText(poiItem.getTitle());
        viewHolder.mTextAddress.setText(poiItem.getSnippet());
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public void setXRecyclerViewOnItemClick(XRecyclerViewOnItemClick xRecyclerViewOnItemClick) {
        this.mXRecyclerViewOnItemClick = xRecyclerViewOnItemClick;
    }
}
