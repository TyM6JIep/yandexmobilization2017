package com.yandexmobilization.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.yandexmobilization.R;
import com.yandexmobilization.models.HistoryItem;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class HistoryView extends RecyclerView.ViewHolder {

    private ImageView mImageBookmark;
    private TextView mText;
    private TextView mTranslate;
    private TextView mLang;

    SwipeRevealLayout mSwipeLayout;
    View mButtonLayout;
    View mItemWrapper;

    public HistoryView(View itemView) {
        super(itemView);

        mImageBookmark = (ImageView) itemView.findViewById(R.id.image_bookmark);

        mText = (TextView) itemView.findViewById(R.id.text_text);
        mTranslate = (TextView) itemView.findViewById(R.id.text_translate);
        mLang = (TextView) itemView.findViewById(R.id.text_language);

        mSwipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe);
        mButtonLayout = itemView.findViewById(R.id.bottom_wrapper);
        mItemWrapper = itemView.findViewById(R.id.item_wrapper);
    }

    public void loadItem(HistoryItem item) {
        mSwipeLayout.close(false);

        if (item.isFavorite()) {
            mImageBookmark.setImageResource(R.drawable.ic_bookmark);
        } else {
            mImageBookmark.setImageResource(R.drawable.ic_bookmark_empty);
        }

        mText.setText(item.getText());
        mTranslate.setText(item.getTranslate());
        mLang.setText(item.getLang());
    }

    public SwipeRevealLayout getSwipeLayout() {
        return mSwipeLayout;
    }

    public View getButtonLayout() {
        return mButtonLayout;
    }

    public View getItemWrapper() {
        return mItemWrapper;
    }
}