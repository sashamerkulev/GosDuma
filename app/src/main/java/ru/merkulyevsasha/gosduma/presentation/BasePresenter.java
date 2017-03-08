package ru.merkulyevsasha.gosduma.presentation;


import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;


public class BasePresenter {

    @Inject
    public DatabaseHelper mDatabase;

    public BasePresenter(){

        GosDumaApp.getComponent().inject(this);
    }

}
