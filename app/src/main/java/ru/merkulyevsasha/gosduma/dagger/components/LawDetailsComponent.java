package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.LawDetailsModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawDetailsScope;

@LawDetailsScope
@Subcomponent(modules={LawDetailsModule.class})
public interface LawDetailsComponent {

    @Subcomponent.Builder
    interface Builder {
        LawDetailsComponent.Builder module(LawDetailsModule module);
        LawDetailsComponent build();
    }

}
