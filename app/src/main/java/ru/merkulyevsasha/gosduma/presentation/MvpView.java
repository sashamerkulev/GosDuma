package ru.merkulyevsasha.gosduma.presentation;


public interface MvpView {

    void showMessage(String message);

    void hideProgress();
    void showProgress();

}
