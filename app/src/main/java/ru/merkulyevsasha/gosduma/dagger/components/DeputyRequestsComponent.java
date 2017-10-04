package ru.merkulyevsasha.gosduma.dagger.components;



import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.DeputyRequestsModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyRequestsScope;

@DeputyRequestsScope
@Subcomponent(modules={DeputyRequestsModule.class})
public interface DeputyRequestsComponent {

    @Subcomponent.Builder
    interface Builder {
        DeputyRequestsComponent.Builder module(DeputyRequestsModule module);
        DeputyRequestsComponent build();
    }
}
