package com.app.fixirman.di.module;

import com.app.fixirman.view.activity.ChangeAddressActivity;
import com.app.fixirman.view.activity.ChatActivity;
import com.app.fixirman.view.activity.FAQActivity;
import com.app.fixirman.view.activity.LocationPickerActivity;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.activity.PhoneActivity;
import com.app.fixirman.view.activity.ProviderDetailsActivity;
import com.app.fixirman.view.activity.RequestDetailActivity;
import com.app.fixirman.view.activity.SplashActivity;
import com.app.fixirman.view.activity.UpdateProfileActivity;
import com.app.fixirman.view.create_request.CreateRequestActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract PhoneActivity contributePhoneActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract LocationPickerActivity contributeLocationPickerActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ChatActivity contributeChatActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract UpdateProfileActivity contributeUpdateProfileActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ChangeAddressActivity contributeChangeAddressActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ProviderDetailsActivity contributeProviderDetailsActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract FAQActivity contributeFAQActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract CreateRequestActivity contributeCreateRequestActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract RequestDetailActivity contributeRequestDetailActivity();

}
