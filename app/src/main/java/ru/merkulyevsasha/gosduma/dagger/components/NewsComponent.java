package ru.merkulyevsasha.gosduma.dagger.components;


import dagger.Subcomponent;
import ru.merkulyevsasha.gosduma.dagger.modules.NewsModule;
import ru.merkulyevsasha.gosduma.dagger.scopes.NewsScope;

@NewsScope
@Subcomponent(modules={NewsModule.class})
public interface NewsComponent {

    @Subcomponent.Builder
    interface Builder {
        NewsComponent.Builder module(NewsModule module);
        NewsComponent build();
    }
}
