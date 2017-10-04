package ru.merkulyevsasha.gosduma.dagger.components;



import android.content.Context;

import com.evernote.android.job.JobCreator;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.dagger.modules.AppModule;

@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);
        AppComponent build();
    }

    void inject(GosDumaApp app);

    JobCreator getJobCreator();
}
