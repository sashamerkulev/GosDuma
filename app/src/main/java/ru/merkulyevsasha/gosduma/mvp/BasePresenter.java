package ru.merkulyevsasha.gosduma.mvp;


import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;


public class BasePresenter {

    @Inject
    public DatabaseHelper mDatabase;

    public BasePresenter(){

    }

}
