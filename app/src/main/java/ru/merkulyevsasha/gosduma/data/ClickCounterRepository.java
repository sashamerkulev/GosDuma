package ru.merkulyevsasha.gosduma.data;



public interface ClickCounterRepository {

    boolean canShowInterstitialAd();
    void resetCounter();
    void incrementCounter();

}
