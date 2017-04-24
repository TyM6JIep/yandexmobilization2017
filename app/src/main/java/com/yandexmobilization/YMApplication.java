package com.yandexmobilization;

import android.app.Application;
import android.content.Intent;

import com.orm.SugarContext;
import com.yandexmobilization.net.HttpClient;
import com.yandexmobilization.services.AppService;

/**
 * Created by YuriFadeev on 24.04.17.
 */

public class YMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        SugarContext.init(this);
        startService(new Intent(this, AppService.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        SugarContext.terminate();
        HttpClient.clear();
    }
}
