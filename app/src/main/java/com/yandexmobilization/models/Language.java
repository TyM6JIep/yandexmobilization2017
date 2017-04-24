package com.yandexmobilization.models;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class Language {
    private String code;
    private String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
