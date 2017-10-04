package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.DeputyDetailsModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputyDetailsScope;

@DeputyDetailsScope
@Subcomponent(modules={DeputyDetailsModule.class})
public interface DeputyDetailsComponent {

    @Subcomponent.Builder
    interface Builder {
        DeputyDetailsComponent.Builder module(DeputyDetailsModule module);
        DeputyDetailsComponent build();
    }
}
