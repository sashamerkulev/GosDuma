package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.LawsModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.LawsScope;

@LawsScope
@Subcomponent(modules={LawsModule.class})
public interface LawsComponent {

    @Subcomponent.Builder
    interface Builder {
        LawsComponent.Builder module(LawsModule module);
        LawsComponent build();
    }
}
