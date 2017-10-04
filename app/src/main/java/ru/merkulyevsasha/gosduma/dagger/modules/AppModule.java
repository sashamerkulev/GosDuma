package ru.merkulyevsasha.gosduma.dagger.modules;

import android.content.Context;

import com.evernote.android.job.JobCreator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.merkulyevsasha.gosduma.dagger.components.ListDataComponent;
import ru.merkulyevsasha.gosduma.dagger.components.MainComponent;
import ru.merkulyevsasha.gosduma.dagger.components.NewsComponent;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyDetailsScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawDetailsScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.ListDataScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.MainScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.NewsScope;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepository;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.AktRssService;
import ru.merkulyevsasha.gosduma.data.http.NewsRssService;
import ru.merkulyevsasha.gosduma.data.preferences.SettingsSharedPreferences;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractorImpl;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.presentation.main.MainActivity;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;
import ru.merkulyevsasha.gosduma.presentation.services.GDJobCreator;

@Module(includes = {AndroidSupportInjectionModule.class},
        subcomponents = {MainComponent.class, ListDataComponent.class, NewsComponent.class})
public abstract class AppModule {

    @Singleton
    @Provides
    static ExecutorService providesExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() );
    }

    @Singleton
    @Provides
    static Scheduler providesScheduler() {
        return Schedulers.io();
    }

    @Singleton
    @Provides
    static DatabaseHelper providesDatabaseHelper(Context context) {
        return new DatabaseHelper(DatabaseHelper.getDbPath(context));
    }

    @Singleton
    @Provides
    static SettingsSharedPreferences providesSettingsSharedPreferences(Context context) {
        return new SettingsSharedPreferences(context);
    }

    @Singleton
    @Provides
    static NewsRssService providesRssService() {
        return new NewsRssService();
    }

    @Singleton
    @Provides
    static AktRssService providesAktRssService() {
        return new AktRssService();
    }

    @Singleton
    @Binds
    abstract ClickCounterRepository providesClickCounterRepository(ClickCounterRepositoryImpl repository);

    @Singleton
    @Binds
    abstract NewsInteractor providesNewsInteractor(NewsInteractorImpl interactor);

    @Singleton
    @Binds
    abstract NewsRepository providesNewsRepository(NewsRepositoryImpl repository);

    @Singleton
    @Binds
    abstract NewsServiceInteractor providesNewsServiceInteractor(NewsServiceInteractorImpl interactor);

    @Singleton
    @Binds
    abstract JobCreator jobCreator(GDJobCreator creator);

    @MainScope
    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity mainInjector();

    @NewsScope
    @ContributesAndroidInjector(modules = {NewsModule.class})
    abstract NewsActivity newsInjector();

    @ListDataScope
    @ContributesAndroidInjector(modules = {ListDataModule.class})
    abstract ListDataActivity listDataInjector();

    @LawDetailsScope
    @ContributesAndroidInjector(modules = {LawDetailsModule.class})
    abstract LawDetailsActivity lawDetailsInjector();

    @LawDetailsScope
    @ContributesAndroidInjector(modules = {LawDetailsModule.class})
    abstract LawDetailsFragment lawDetailsFragmentInjector();

    @DeputyDetailsScope
    @ContributesAndroidInjector(modules = {DeputyDetailsModule.class})
    abstract DeputyDetailsActivity deputyDetailsInjector();

    @DeputyDetailsScope
    @ContributesAndroidInjector(modules = {DeputyDetailsModule.class})
    abstract DeputyDetailsFragment deputyDetailsFragmentInjector();
}
