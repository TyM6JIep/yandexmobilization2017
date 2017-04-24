package com.yandexmobilization.models;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.yandexmobilization.R;
import com.yandexmobilization.net.HttpClient;
import com.yandexmobilization.responses.ResponseCallback;
import com.yandexmobilization.utils.Constants;

import java.util.HashMap;
import java.util.List;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class SupportLanguage {
    private List<String> dirs;
    private HashMap<String, String> langs;

    public List<String> getDirs() {
        return dirs;
    }

    public HashMap<String, String> getLangs() {
        return langs;
    }

    public static void getLanguages(Context context, String langCode, ResponseCallback<SupportLanguage> callback) {
        RequestParams params = new RequestParams();
        params.add(Constants.API_PARAM_KEY, context.getString(R.string.yandex_translator_api_key));
        params.add(Constants.API_PARAM_UI, langCode);

        HttpClient.get(context, Constants.API_URL_GET_LANGS, params, callback);
    }

    public static void translate(Context context, String text, String code, ResponseCallback<Translation> callback) {
        RequestParams params = new RequestParams();
        params.add(Constants.API_PARAM_KEY, context.getString(R.string.yandex_translator_api_key));
        params.add(Constants.API_PARAM_TEXT, text);
        params.add(Constants.API_PARAM_LANG, code);
        params.add(Constants.API_PARAM_FORMAT, Constants.API_PARAM_FORMAT_PLAIN);
        HttpClient.post(context, Constants.API_URL_TRANSLATE, params, callback);
    }
}
