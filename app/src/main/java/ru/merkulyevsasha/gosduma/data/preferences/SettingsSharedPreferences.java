package ru.merkulyevsasha.gosduma.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsSharedPreferences {

    public final static int MAX_COUNT = 3;

    private final static String KEY_FIRST_RUN = "ru.merkulyevsasha.gosduma.prefs_key_run_flag";

    private final SharedPreferences prefs;

    public SettingsSharedPreferences(Context context){
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public int getClickCounter(){
        return prefs.getInt("clicks", 0);
    }

    public void incClickCounter(){
        int clicks = prefs.getInt("clicks", 0);
        clicks++;
        if (clicks == MAX_COUNT){
            clicks = 0;
        }
        prefs.edit().putInt("clicks", clicks).apply();
    }

    public void resetClickCounter(){
        prefs.edit().putInt("clicks", 0).apply();
    }

    public boolean getFirstRunFlag() {
        return prefs.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstRunFlag() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_FIRST_RUN, false);
        editor.apply();
    }
}
