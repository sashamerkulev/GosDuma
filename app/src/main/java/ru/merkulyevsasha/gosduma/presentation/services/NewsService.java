package ru.merkulyevsasha.gosduma.presentation.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;


public class NewsService extends Service {

    @Inject
    ExecutorService executor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        GosDumaApp.getComponent().inject(this);

        System.out.println("NewsService: start service");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            System.out.println("NewsService: stop service: wifi is disabled");
            ServicesHelper.registerAlarmNewsService(this);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_HEALTH_GOOD;
        if (!isCharging){
            System.out.println("NewsService: stop service: battery low");
            ServicesHelper.registerAlarmNewsService(this);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        System.out.println("NewsService: executor submit");
        executor.submit(new NewsRunnable(this.getApplicationContext()));
        System.out.println("NewsService: register alarm");
        ServicesHelper.registerAlarmNewsService(this);

        return super.onStartCommand(intent, flags, startId);
    }
}
