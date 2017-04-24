package com.yandexmobilization.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yandexmobilization.R;
import com.yandexmobilization.interfaces.LanguageSelectListener;
import com.yandexmobilization.models.Language;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class LanguageView extends RecyclerView.ViewHolder {

    private TextView mName;
    private Language mLanguage;
    private int mType;

    public LanguageView(View itemView, final LanguageSelectListener listener) {
        super(itemView);

        mName = (TextView) itemView.findViewById(R.id.text_language);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelectLanguage(mLanguage, mType);
                }
            }
        });
    }

    public void loadLanguage(Language language, int type) {
        mLanguage = language;
        mType = type;
        mName.setText(language.getName());
    }
}