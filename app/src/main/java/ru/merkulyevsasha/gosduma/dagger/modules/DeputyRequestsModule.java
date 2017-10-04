package ru.merkulyevsasha.gosduma.dagger.modules;


import dagger.Binds;
import dagger.Module;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepository;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepositoryImpl;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyRequestsScope;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractorImpl;

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class DeputyRequestsModule {

    @DeputyRequestsScope
    @Binds
    abstract DeputyRequestsRepository providesDeputyRequestsRepository(DeputyRequestsRepositoryImpl repository);

    @DeputyRequestsScope
    @Binds
    abstract DeputyRequestsInteractor providesDeputyRequestsInteractor(DeputyRequestsInteractorImpl interactor);
}
