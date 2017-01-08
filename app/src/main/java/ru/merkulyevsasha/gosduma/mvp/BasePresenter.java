package ru.merkulyevsasha.gosduma.mvp;


import android.content.Context;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.dagger.AppModule;
import ru.merkulyevsasha.gosduma.dagger.DaggerDbComponent;
import ru.merkulyevsasha.gosduma.dagger.DbComponent;
import ru.merkulyevsasha.gosduma.dagger.DbModule;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;


public class BasePresenter {

    @Inject
    public DatabaseHelper mDatabase;

    public BasePresenter(Context context){

        DbComponent component = DaggerDbComponent.builder()
                .appModule(new AppModule(context))
                .dbModule(new DbModule())
                .build();

        component.inject(this);
    }

}
