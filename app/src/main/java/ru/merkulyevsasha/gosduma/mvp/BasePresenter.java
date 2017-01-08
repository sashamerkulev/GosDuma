package ru.merkulyevsasha.gosduma.mvp;


import android.content.Context;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;


public class BasePresenter {

    @Inject
    public DatabaseHelper mDatabase;

    public BasePresenter(Context context){

        GosDumaApp.getComponent().inject(this);
    }

}
