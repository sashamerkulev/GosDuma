package ru.merkulyevsasha.gosduma.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsSharedPreferences {

    public final static int MAX_COUNT = 3;
    private final SharedPreferences pref;

    public SettingsSharedPreferences(Context context){
        pref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences(){
        return pref;
    }

    public int getClickCounter(){
        return pref.getInt("clicks", 0);
    }

    public void incClickCounter(){
        int clicks = pref.getInt("clicks", 0);
        clicks++;
        if (clicks == MAX_COUNT){
            clicks = 0;
        }
        pref.edit().putInt("clicks", clicks).apply();
    }

    public void resetClickCounter(){
        pref.edit().putInt("clicks", 0).apply();
    }

}
