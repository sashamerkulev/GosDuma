package ru.merkulyevsasha.gdcore.preferences;

public interface SettingsSharedPreferences {
    int getClickCounter();

    void incClickCounter();

    void resetClickCounter();

    boolean getFirstRunFlag();

    void setFirstRunFlag();
}
