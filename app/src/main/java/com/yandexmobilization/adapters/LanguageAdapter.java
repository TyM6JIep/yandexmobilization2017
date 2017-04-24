package com.yandexmobilization.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandexmobilization.R;
import com.yandexmobilization.interfaces.LanguageSelectListener;
import com.yandexmobilization.models.Language;
import com.yandexmobilization.views.LanguageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageView> {

    private List<Language> mData = new ArrayList<>();
    private LanguageSelectListener mListener;
    private int mType;

    public LanguageAdapter(LanguageSelectListener listener) {
        this.mListener = listener;
    }

    @Override
    public LanguageView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_language, parent, false);
        return new LanguageView(view, mListener);
    }

    @Override
    public void onBindViewHolder(LanguageView holder, int position) {
        Language language = mData.get(position);
        holder.loadLanguage(language, mType);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void loadAddress(List<Language> list) {
        mData.addAll(list);
    }

    public void clear() {
        mData.clear();
    }

    public void setType(int type) {
        mType = type;
    }

    public List<Language> getData() {
        return mData;
    }
}