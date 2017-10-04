package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.DeputiesModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.DeputiesScope;

@DeputiesScope
@Subcomponent(modules={DeputiesModule.class})
public interface DeputiesComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder module(DeputiesModule module);
        DeputiesComponent build();
    }
}
