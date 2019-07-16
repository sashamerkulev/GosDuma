package ru.merkulyevsasha.data;


import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences;
import ru.merkulyevsasha.gdcore.repositories.ClickCounterRepository;

public class ClickCounterRepositoryImpl implements ClickCounterRepository {

    private final static int MAX_COUNT = 3;

    private SettingsSharedPreferences pref;

    ClickCounterRepositoryImpl(SettingsSharedPreferences pref) {
        this.pref = pref;
    }

    @Override
    public boolean canShowInterstitialAd() {

        int clicks = pref.getClickCounter();
        return ((clicks + 1) == MAX_COUNT);
    }

    @Override
    public void incrementCounter() {
        pref.incClickCounter();
    }

    @Override
    public void resetCounter() {
        pref.resetClickCounter();
    }

}
