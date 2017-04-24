package com.yandexmobilization.responses;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public interface ResponseCallback<T> {
    void onSuccess(T response);
    void onError(Error error);
}
