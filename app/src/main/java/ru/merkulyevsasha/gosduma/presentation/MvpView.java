package ru.merkulyevsasha.gosduma.presentation;


import android.support.annotation.StringRes;

public interface MvpView {

    void showMessage(@StringRes int resId);

    void hideProgress();
    void showProgress();

}
