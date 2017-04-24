package com.yandexmobilization.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yandexmobilization.net.HttpClient;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class AppService extends Service {

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        HttpClient.clear();
        stopSelf();
    }
}