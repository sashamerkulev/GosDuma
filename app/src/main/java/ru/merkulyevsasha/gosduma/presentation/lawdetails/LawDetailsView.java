package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import java.util.HashMap;

import ru.merkulyevsasha.gosduma.presentation.MvpView;


public interface LawDetailsView extends MvpView {

    void showData(HashMap<String, String> result);
    void showEmptyData();

}
