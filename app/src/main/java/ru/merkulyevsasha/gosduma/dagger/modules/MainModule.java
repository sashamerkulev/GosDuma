package ru.merkulyevsasha.gosduma.dagger.modules;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.merkulyevsasha.gosduma.dagger.components.DeputiesComponent;
import ru.merkulyevsasha.gosduma.dagger.components.DeputyRequestsComponent;
import ru.merkulyevsasha.gosduma.dagger.components.LawsComponent;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputiesScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyRequestsScope;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawsScope;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesFragment;
import ru.merkulyevsasha.gosduma.presentation.deputyrequests.DeputyRequestsFragment;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsFragment;

@Module(includes = {AndroidSupportInjectionModule.class},
        subcomponents = {DeputiesComponent.class, DeputyRequestsComponent.class, LawsComponent.class})
public abstract class MainModule {

    @DeputiesScope
    @ContributesAndroidInjector(modules = {DeputiesModule.class})
    abstract DeputiesFragment deputiesInjector();

    @LawsScope
    @ContributesAndroidInjector(modules = {LawsModule.class})
    abstract LawsFragment lawsInjector();

    @DeputyRequestsScope
    @ContributesAndroidInjector(modules = {DeputyRequestsModule.class})
    abstract DeputyRequestsFragment deputyRequestsInjector();

}
