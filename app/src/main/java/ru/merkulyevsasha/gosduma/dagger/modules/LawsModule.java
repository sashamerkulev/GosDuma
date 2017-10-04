package ru.merkulyevsasha.gosduma.dagger.modules;



import dagger.Binds;
import dagger.Module;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.dagger.components.LawDetailsComponent;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawsScope;
import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.data.LawsRepositoryImpl;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.domain.LawsInteractorImpl;

@Module(includes = {AndroidSupportInjectionModule.class}, subcomponents = {LawDetailsComponent.class})
public abstract class LawsModule {

    @LawsScope
    @Binds
    abstract LawsRepository providesLawsRepository(LawsRepositoryImpl repository);

    @LawsScope
    @Binds
    abstract LawsInteractor providesLawsInteractor(LawsInteractorImpl interactor);

}
