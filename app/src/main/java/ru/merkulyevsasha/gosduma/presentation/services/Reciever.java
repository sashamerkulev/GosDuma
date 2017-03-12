package ru.merkulyevsasha.gosduma.presentation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Reciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)
                || action.equals("android.intent.action.QUICKBOOT_POWERON")
                || action.equals("com.htc.intent.action.QUICKBOOT_POWERON") ) {

            ServicesHelper.register(context);

        } else {
            System.out.println("reciever: start service");
            context.startService(new Intent(context, NewsService.class));
        }

    }
}
