package ru.merkulyevsasha.gosduma.data;


import ru.merkulyevsasha.gosduma.data.preferences.SettingsSharedPreferences;

public class ClickCounterRepositoryImpl implements ClickCounterRepository {

    private SettingsSharedPreferences pref;

    public ClickCounterRepositoryImpl(SettingsSharedPreferences pref){
        this.pref = pref;
    }

    @Override
    public boolean canShowInterstitialAd(){

        int clicks = pref.getClickCounter();
        return ((clicks+1) == SettingsSharedPreferences.MAX_COUNT);
    }

    @Override
    public void incrementCounter(){
        pref.incClickCounter();
    }

    @Override
    public void resetCounter(){
        pref.resetClickCounter();
    }

}
