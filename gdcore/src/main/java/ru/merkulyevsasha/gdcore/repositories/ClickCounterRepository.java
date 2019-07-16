package ru.merkulyevsasha.gdcore.repositories;



public interface ClickCounterRepository {

    boolean canShowInterstitialAd();
    void resetCounter();
    void incrementCounter();

}
