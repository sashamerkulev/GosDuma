package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.MainModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.MainScope;

@MainScope
@Subcomponent(modules={MainModule.class})
public interface MainComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder module(MainModule module);
        MainComponent build();
    }
}
