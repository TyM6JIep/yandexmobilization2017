package com.yandexmobilization.models;

import java.util.List;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class Translation {
    private int code;
    private String lang;
    private List<String> text;

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public List<String> getText() {
        return text;
    }
}
