package ru.merkulyevsasha.gosduma.di;



import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.RssService;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.presentation.news.NewsPresenter;

@Module
public class PresentersModule {

    @Singleton
    @Provides
    NewsPresenter providesNewsPresenter(NewsInteractor inter) {
        return new NewsPresenter(inter);
    }


}
