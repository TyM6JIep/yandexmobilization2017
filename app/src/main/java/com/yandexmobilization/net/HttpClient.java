package com.yandexmobilization.net;

import android.content.Context;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yandexmobilization.helpers.NetworkHelper;
import com.yandexmobilization.interfaces.ConnectionListener;
import com.yandexmobilization.responses.Error;
import com.yandexmobilization.responses.ResponseCallback;
import com.yandexmobilization.utils.Constants;
import com.yandexmobilization.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class HttpClient {

    private static Gson mGson;
    private static List<ConnectionListener> mListeners = new ArrayList<>();

    private static AsyncHttpClient syncHttpClient= new SyncHttpClient(true, 80, 443);
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true, 80, 443);

    private static AsyncHttpClient getClient() {
        AsyncHttpClient client = asyncHttpClient;
        if (Looper.myLooper() == null) {
            client = syncHttpClient;
        }
        client.setTimeout(30*1000*10);
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

        return client;
    }

    private static Gson getGson() {
        if (mGson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            mGson = gsonBuilder.create();
        }
        return mGson;
    }

    private static String getUrl(String url) {
        return Constants.API_URL + url;
    }

    public static <T> void post(Context context, String url, RequestParams params, final ResponseCallback<T> callback) {
        if(!NetworkHelper.isConnected(context)) {
            connectionFailed();
            return;
        }
        getClient().get(getUrl(url), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                T resp = getGson().fromJson(response.toString(), Utils.getGenericType(callback));
                callback.onSuccess(resp);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Error error = getGson().fromJson(errorResponse.toString(), Error.class);
                callback.onError(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Error error;
                if (throwable != null) {
                    error = new Error(statusCode, throwable.getLocalizedMessage());
                } else {
                    error = new Error(statusCode, responseString);
                }
                callback.onError(error);
            }
        });
    }

    public static <T> void get(Context context, String url, RequestParams params, final ResponseCallback<T> callback){
        if(!NetworkHelper.isConnected(context)) {
            connectionFailed();
            return;
        }
        getClient().get(getUrl(url), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                T resp = getGson().fromJson(response.toString(), Utils.getGenericType(callback));
                callback.onSuccess(resp);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Error error = getGson().fromJson(errorResponse.toString(), Error.class);
                callback.onError(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Error error;
                if (throwable != null) {
                    error = new Error(statusCode, throwable.getLocalizedMessage());
                } else {
                    error = new Error(statusCode, responseString);
                }
                callback.onError(error);
            }
        });
    }

    private static void connectionFailed() {
        for (ConnectionListener listener: mListeners) {
            listener.onFailure();
        }
    }

    public static void clear() {
        if (mListeners != null) {
            mListeners.clear();
        }
    }

    public static void addConnectionListener(ConnectionListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public static void removeConnectionListener(ConnectionListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }
}
