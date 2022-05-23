package com.app.fixirman.di.module;

import com.app.fixirman.view.create_request.RequestScheduleFragment;
import com.app.fixirman.view.create_request.RequestSummaryFragment;
import com.app.fixirman.view.create_request.SelectServiceFragment;
import com.app.fixirman.view.fragment.AppointmentDetailFragment;
import com.app.fixirman.view.fragment.AppointmentFragment;
import com.app.fixirman.view.fragment.CategoriesFragment;
import com.app.fixirman.view.fragment.InboxFragment;
import com.app.fixirman.view.fragment.NotificationsFragment;
import com.app.fixirman.view.fragment.ProvidersFragment;
import com.app.fixirman.view.fragment.SubCategoryFragment;
import com.app.fixirman.view.fragment.UserDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract UserDetailFragment contributeUserDetailFragment();

    @ContributesAndroidInjector
    abstract AppointmentFragment contributeAppointmentFragment();

    @ContributesAndroidInjector
    abstract AppointmentDetailFragment contributeAppointmentDetailFragment();

    @ContributesAndroidInjector
    abstract NotificationsFragment contributeNotificationsFragment();

    @ContributesAndroidInjector
    abstract CategoriesFragment contributeCategoriesFragment();

    @ContributesAndroidInjector
    abstract SubCategoryFragment contributeSubCategoryFragment();

    @ContributesAndroidInjector
    abstract ProvidersFragment contributeProvidersFragment();

    @ContributesAndroidInjector
    abstract InboxFragment contributeInboxFragment();

    @ContributesAndroidInjector
    abstract RequestSummaryFragment contributeRequestSummaryFragment();

    @ContributesAndroidInjector
    abstract RequestScheduleFragment contributeRequestScheduleFragment();

    @ContributesAndroidInjector
    abstract SelectServiceFragment contributeSelectServiceFragment();
}
