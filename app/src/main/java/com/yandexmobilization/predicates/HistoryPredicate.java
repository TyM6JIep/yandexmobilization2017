package com.yandexmobilization.predicates;

import com.yandexmobilization.models.HistoryItem;

import org.apache.commons.collections4.Predicate;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class HistoryPredicate implements Predicate {

    private String mText;
    private boolean mChecked;

    public HistoryPredicate(String text, boolean checked) {
        mText = text;
        mChecked = checked;
    }

    @Override
    public boolean evaluate(Object object) {
        HistoryItem item = (HistoryItem) object;
        if (mChecked) {
            return ((item.getText().toLowerCase().contains(mText.toLowerCase()) || item.getTranslate().toLowerCase().contains(mText.toLowerCase())) && item.isFavorite());
        } else {
            return (item.getText().toLowerCase().contains(mText.toLowerCase()) || item.getTranslate().toLowerCase().contains(mText.toLowerCase()));
        }
    }
}