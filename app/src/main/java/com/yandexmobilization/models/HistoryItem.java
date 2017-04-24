package com.yandexmobilization.models;

import com.orm.SugarRecord;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class HistoryItem extends SugarRecord {

    private final int FAVORITE_TRUE = 1;

    private String text;
    private String translate;
    private String lang;
    private int isFavorite = 0;

    public HistoryItem() {
    }

    public HistoryItem(String text, String translate, String lang) {
        this.text = text;
        this.translate = translate;
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    public String getLang() {
        return lang;
    }

    public Boolean isFavorite() {
        return isFavorite == FAVORITE_TRUE;
    }

    public void setFavorite() {
        this.isFavorite = FAVORITE_TRUE;
    }
}
