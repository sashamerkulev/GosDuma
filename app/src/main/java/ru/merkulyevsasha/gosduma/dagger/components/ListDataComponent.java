package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.ListDataModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.ListDataScope;

@ListDataScope
@Subcomponent(modules={ListDataModule.class})
public interface ListDataComponent {

    @Subcomponent.Builder
    interface Builder {
        ListDataComponent.Builder module(ListDataModule module);
        ListDataComponent build();
    }
}
