package com.yandexmobilization.predicates;

import com.yandexmobilization.models.Language;

import org.apache.commons.collections4.Predicate;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class LanguagePredicate implements Predicate {

    private String mLanguage;

    public LanguagePredicate(String language) {
        mLanguage = language;
    }

    @Override
    public boolean evaluate(Object object) {
        Language language = (Language) object;
        return language.getName().toLowerCase().contains(mLanguage.toLowerCase());
    }
}