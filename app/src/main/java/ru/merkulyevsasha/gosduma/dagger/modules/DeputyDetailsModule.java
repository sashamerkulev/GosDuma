package ru.merkulyevsasha.gosduma.dagger.modules;


import dagger.Binds;
import dagger.Module;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyDetailsScope;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepository;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepositoryImpl;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractorImpl;

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class DeputyDetailsModule {


    @DeputyDetailsScope
    @Binds
    abstract DeputyDetailsRepository providesDeputyDetailsRepository(DeputyDetailsRepositoryImpl repository);

    @DeputyDetailsScope
    @Binds
    abstract DeputyDetailsInteractor providesDeputyDetailsInteractor(DeputyDetailsInteractorImpl interactor);

}
