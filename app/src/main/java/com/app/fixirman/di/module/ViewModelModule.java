package com.app.fixirman.di.module;



import com.app.fixirman.di.key.ViewModelKey;
import com.app.fixirman.view.create_request.RequestViewModel;
import com.app.fixirman.viewmodel.AppointmentViewModel;
import com.app.fixirman.viewmodel.CategoryViewModel;
import com.app.fixirman.viewmodel.ChatViewModel;
import com.app.fixirman.viewmodel.FAQViewModel;
import com.app.fixirman.viewmodel.NotificationViewModel;
import com.app.fixirman.viewmodel.UserViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppointmentViewModel.class)
    abstract ViewModel bindAppointmentViewModel(AppointmentViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    abstract ViewModel bindCategoryViewModel(CategoryViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel bindChatViewModel(ChatViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FAQViewModel.class)
    abstract ViewModel bindFAQViewModel(FAQViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RequestViewModel.class)
    abstract ViewModel bindRequestViewModel(RequestViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
