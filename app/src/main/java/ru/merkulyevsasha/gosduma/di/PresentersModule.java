package ru.merkulyevsasha.gosduma.di;



import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesPresenter;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsPresenter;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataPresenter;
import ru.merkulyevsasha.gosduma.presentation.news.NewsPresenter;

@Module
public class PresentersModule {

    @Singleton
    @Provides
    NewsPresenter providesNewsPresenter(NewsInteractor inter) {
        return new NewsPresenter(inter);
    }

    @Singleton
    @Provides
    ListDataPresenter providesListDataPresenter(ListDataInteractor inter) {
        return new ListDataPresenter(inter);
    }

    @Singleton
    @Provides
    LawsPresenter providesLawsPresenter(LawsInteractor inter) {
        return new LawsPresenter(inter);
    }

    @Singleton
    @Provides
    DeputiesPresenter providesDeputiesPresenter(Context context, DeputiesInteractor inter) {
        return new DeputiesPresenter(context, inter);
    }

}
