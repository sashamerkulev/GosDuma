package ru.merkulyevsasha.gosduma.di;



import javax.inject.Singleton;

import dagger.Component;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesFragment;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.deputyrequests.DeputyRequestsFragment;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.DeputyLawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsFragment;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;

@Singleton
@Component(modules={AppModule.class, DbModule.class, RepositoriesModule.class, InteractorsModule.class, PresentersModule.class})
public interface DbComponent {

    void inject(NewsActivity context);
    void inject(ListDataActivity context);

    void inject(LawsFragment context);
    void inject(DeputiesFragment context);
    void inject(DeputyRequestsFragment context);

    void inject(LawDetailsFragment context);
    void inject(LawDetailsActivity context);
    void inject(DeputyLawDetailsActivity context);

    void inject(DeputyDetailsActivity context);
    void inject(DeputyDetailsFragment context);


}
