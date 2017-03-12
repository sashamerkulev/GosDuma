package ru.merkulyevsasha.gosduma.presentation.services;


import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractor;
import ru.merkulyevsasha.gosduma.models.News;

public class NewsRunnable implements Runnable{


    @Inject
    NewsServiceInteractor inter;

    private WeakReference<Context> context;

    public NewsRunnable(Context context){
        this.context = new WeakReference<Context>(context);
        GosDumaApp.getComponent().inject(this);
    }

    @Override
    public void run() {

        Context cont = context.get();
        if (cont == null)
            return;

        List<News> news = inter.getNotificationNews();
        for(int i = 0; i < news.size(); i++) {
            News n = news.get(0);
            ServicesHelper.setNotification(cont, n.getNavId(), n.getTitle(), n.getName());
        }
    }
}
