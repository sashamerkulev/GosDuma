package ru.merkulyevsasha.gosduma.dagger.modules;


import dagger.Binds;
import dagger.Module;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.data.ListDataRepositoryImpl;
import ru.merkulyevsasha.gosduma.dagger.scopes.ListDataScope;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractorImpl;

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class ListDataModule {

    @ListDataScope
    @Binds
    abstract ListDataRepository providesListDataRepository(ListDataRepositoryImpl repository);

    @ListDataScope
    @Binds
    abstract ListDataInteractor providesListDataInteractor(ListDataInteractorImpl interactor);

}
