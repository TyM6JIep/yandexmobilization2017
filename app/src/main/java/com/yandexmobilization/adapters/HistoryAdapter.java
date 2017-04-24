package com.yandexmobilization.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.yandexmobilization.HistoryActivity;
import com.yandexmobilization.R;
import com.yandexmobilization.models.HistoryItem;
import com.yandexmobilization.views.HistoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryView> {

    private final Context mContext;
    private final List<HistoryItem> mData;

    public HistoryAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(List<HistoryItem> historyItems) {
        mData.clear();
        mData.addAll(historyItems);
        notifyDataSetChanged();
    }

    @Override
    public HistoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new HistoryView(view);
    }

    @Override
    public void onBindViewHolder(final HistoryView holder, @SuppressLint("RecyclerView") final int position) {
        HistoryItem item = mData.get(position);

        holder.loadItem(item);

        View v = holder.getButtonLayout().findViewById(R.id.layout_remove);
        v.setOnClickListener(null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        holder.getButtonLayout().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.getButtonLayout().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    holder.getButtonLayout().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int height = holder.getItemWrapper().getHeight();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(holder.getButtonLayout().getWidth(), height);
                holder.getButtonLayout().setLayoutParams(params);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        HistoryItem item = mData.get(position);
        item.delete();
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());

        if (mContext instanceof HistoryActivity) {
            ((HistoryActivity)mContext).refresh();
        }
    }

    public List<HistoryItem> getData() {
        return mData;
    }

    public void clear() {
        mData.clear();
    }
}