package ru.merkulyevsasha.gosduma.dagger.modules;


import dagger.Binds;
import dagger.Module;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawDetailsScope;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepository;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepositoryImpl;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractorImpl;

@Module(includes = {AndroidSupportInjectionModule.class})
public abstract class LawDetailsModule {

    @LawDetailsScope
    @Binds
    abstract LawDetailsRepository providesLawDetailsRepository(LawDetailsRepositoryImpl repository);

    @LawDetailsScope
    @Binds
    abstract LawDetailsInteractor providesLawDetailsInteractor(LawDetailsInteractorImpl interactor);

}
