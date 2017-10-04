package ru.merkulyevsasha.gosduma.dagger.modules;




import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.dagger.components.DeputyDetailsComponent;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputiesScope;
import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.data.DeputiesRepositoryImpl;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractorImpl;

@Module(includes = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class},
        subcomponents = {DeputyDetailsComponent.class})
public abstract class DeputiesModule {

    @DeputiesScope
    @Binds
    abstract DeputiesRepository providesDeputiesRepository(DeputiesRepositoryImpl repository);

    @DeputiesScope
    @Binds
    abstract DeputiesInteractor providesDeputiesInteractor(DeputiesInteractorImpl interactor);

}
