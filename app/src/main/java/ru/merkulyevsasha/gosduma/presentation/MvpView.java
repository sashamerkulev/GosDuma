package ru.merkulyevsasha.gosduma.presentation;


public interface MvpView {

    void showMessage(int resId);

    void hideProgress();
    void showProgress();

}
