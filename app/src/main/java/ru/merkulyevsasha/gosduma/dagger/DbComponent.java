package ru.merkulyevsasha.gosduma.dagger;



import javax.inject.Singleton;

import dagger.Component;
import ru.merkulyevsasha.gosduma.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.mvp.BasePresenter;
import ru.merkulyevsasha.gosduma.news.NewsActivity;

@Singleton
@Component(modules={AppModule.class, DbModule.class})
public interface DbComponent {

    void inject(NewsActivity context);
    void inject(ListDataActivity context);

    void inject(BasePresenter presenter);
}
