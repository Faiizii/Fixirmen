package com.app.fixirman.di.component;

import android.app.Application;

import com.app.fixirman.my_app.MyApplication;
import com.app.fixirman.di.module.ActivityModule;
import com.app.fixirman.di.module.AppModule;
import com.app.fixirman.di.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules={AndroidInjectionModule.class, ActivityModule.class,  FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MyApplication app);
}
