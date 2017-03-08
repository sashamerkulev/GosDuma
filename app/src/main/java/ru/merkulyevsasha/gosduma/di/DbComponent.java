package ru.merkulyevsasha.gosduma.di;



import javax.inject.Singleton;

import dagger.Component;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.presentation.BasePresenter;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;

@Singleton
@Component(modules={AppModule.class, DbModule.class, RepositoriesModule.class, InteractorsModule.class, PresentersModule.class})
public interface DbComponent {

    void inject(NewsActivity context);
    void inject(ListDataActivity context);

    void inject(BasePresenter presenter);


}
