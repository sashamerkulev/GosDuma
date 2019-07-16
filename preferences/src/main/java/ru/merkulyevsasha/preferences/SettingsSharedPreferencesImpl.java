package ru.merkulyevsasha.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences;


public class SettingsSharedPreferencesImpl implements SettingsSharedPreferences {

    private final static int MAX_COUNT = 3;

    private final static String KEY_FIRST_RUN = "prefs_key_run_flag";
    private final static String KEY_CLICKS = "clicks";

    private final SharedPreferences prefs;

    public SettingsSharedPreferencesImpl(Context context) {
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public int getClickCounter() {
        return prefs.getInt(KEY_CLICKS, 0);
    }

    @Override
    public void incClickCounter() {
        int clicks = prefs.getInt(KEY_CLICKS, 0);
        clicks++;
        if (clicks == MAX_COUNT) {
            clicks = 0;
        }
        prefs.edit().putInt(KEY_CLICKS, clicks).apply();
    }

    @Override
    public void resetClickCounter() {
        prefs.edit().putInt(KEY_CLICKS, 0).apply();
    }

    @Override
    public boolean getFirstRunFlag() {
        return prefs.getBoolean(KEY_FIRST_RUN, true);
    }

    @Override
    public void setFirstRunFlag() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_FIRST_RUN, false);
        editor.apply();
    }
}
