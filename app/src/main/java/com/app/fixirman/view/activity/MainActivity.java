package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityMainBinding;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.user.User;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.fragment.AppointmentFragment;
import com.app.fixirman.view.fragment.CategoriesFragment;
import com.app.fixirman.view.fragment.InboxFragment;
import com.app.fixirman.view.fragment.NotificationsFragment;
import com.app.fixirman.view.fragment.UserDetailFragment;
import com.app.fixirman.viewmodel.UserViewModel;
import com.app.fixirman.utils.Crashlytics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener  {
    private final String TAG = "MainActivity";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel viewModel;

    private SessionManager sessionManager;
    private ImageView userImage;
    private TextView username;

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        init();
    }

    private void init() {
        sessionManager = new SessionManager(this);
        sessionManager.setOnChangeListener(this);
        //changeTextSizeToSmall();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        if(AppUtils.isNetworkAvailable(this)){
            viewModel.registerFirebaseListener(sessionManager.getUserId());
        }else{
            Crashlytics.log("main screen open without internet so no listener registered for chat loading");
        }
        initKeyboardObserver();
        showUnreadCount();
        binding.navBottom.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CategoriesFragment()).commit();
        initDrawer();
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);

        userImage = binding.navView.getHeaderView(0).findViewById(R.id.iv_userImage);
        username = binding.navView.getHeaderView(0).findViewById(R.id.tv_userName);
        updateUserDetail(sessionManager.getUser());
    }

    private void initKeyboardObserver() {
        binding.main.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = binding.main.getRootView().getHeight() - binding.main.getHeight();
            if (heightDiff > AppUtils.dpToPx(MainActivity.this, 200)) {
                // if more than 200 dp, it's probably a keyboard...
                binding.navBottom.setVisibility(View.GONE);
                Log.e("KeyBoardState", "Open");
            } else {
                Log.e("KeyBoardState", "Closed");
                binding.navBottom.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showUnreadCount(){
        int menuItemId = binding.navBottom.getMenu().getItem(2).getItemId();
        viewModel.getUnreadCount().observe(this, integer -> {
            if(integer != null && integer > 0){
                BadgeDrawable badge =binding.navBottom.getOrCreateBadge(menuItemId);
                badge.setVerticalOffset(10);
                badge.setHorizontalOffset(10);
                badge.setVisible(true);
                badge.setNumber(integer);
            }else{
                binding.navBottom.removeBadge(menuItemId);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        String tempStr;
        try {
            tempStr = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        } catch (Exception e) {
            tempStr = null;
            Log.e(TAG, "onCreate: ", e);
        }
        int itemId = menuItem.getItemId();
        if (itemId == R.id.navigation_appointments) {
            if (tempStr != null) {
                if (!tempStr.equalsIgnoreCase("appointment"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new AppointmentFragment()).addToBackStack("appointment").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new AppointmentFragment()).addToBackStack("appointment").commit();
            }
        } else if (itemId == R.id.navigation_notifications) {
            if (tempStr != null) {
                if (!tempStr.equalsIgnoreCase("notifications"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new NotificationsFragment()).addToBackStack("notifications").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new NotificationsFragment()).addToBackStack("notifications").commit();
            }
        } else if (itemId == R.id.navigation_categories) {
            if (tempStr != null) {
                if (!tempStr.equalsIgnoreCase("categories"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CategoriesFragment()).addToBackStack("categories").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CategoriesFragment()).addToBackStack("categories").commit();
            }
        } else if (itemId == R.id.nav_account) {
            if (tempStr != null) {
                if (!tempStr.equalsIgnoreCase("user_detail"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new UserDetailFragment()).addToBackStack("user_detail").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new UserDetailFragment()).addToBackStack("user_detail").commit();
            }
        } else if (itemId == R.id.nav_chats) {
            if (tempStr != null) {
                if (!tempStr.equalsIgnoreCase("chats"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new InboxFragment()).addToBackStack("chats").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new InboxFragment()).addToBackStack("chats").commit();
            }
        } else if (itemId == R.id.nav_contact) {
            Intent contactUs = new Intent(this, ContactUsActivity.class);
            startActivity(contactUs);
        } else if (itemId == R.id.nav_privacy) {
            Intent privacyPolicy = new Intent(this, TermsActivity.class);
            startActivity(privacyPolicy);
        } else if (itemId == R.id.nav_about) {
            Intent aboutUs = new Intent(this, AboutUsActivity.class);
            startActivity(aboutUs);
        } else if (itemId == R.id.nav_faq) {
            Intent faq = new Intent(this, FAQActivity.class);
            startActivity(faq);
        } else if (itemId == R.id.nav_logout) {
            if (AppUtils.isNetworkAvailable(this)) {
                logoutOnServer();
            } else {
                AppUtils.createNetworkError(this);
            }
        }
        if (binding.drawerLayout.isOpen()){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
    }
    private void logoutOnServer(){
        showLogoutProgress();
        ApiUtils.getApiInterface().logOut(sessionManager.getUserId(), AppConstants.TYPE_USER, Build.MANUFACTURER, Build.MODEL, "Android", Settings.Secure.ANDROID_ID,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Build.VERSION.RELEASE,sessionManager.getToken(),
                "A", AppConstants.APP_VERSION).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(@NonNull Call<NormalResponse> call, @NonNull Response<NormalResponse> response) {

                hideLogoutProgress();
                if(response.isSuccessful()){
                    NormalResponse normalResponse = response.body();
                    if(normalResponse.getSuccess() == 1){
                        logout();
                    }else{
                        AppUtils.createFailedDialog(MainActivity.this,response.message());
                    }
                }else{
                    logout();
                    Crashlytics.log("code "+response.code());
                    Crashlytics.logException(new Exception("logout api at my account screen"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<NormalResponse> call, @NonNull Throwable t) {
                hideLogoutProgress();
                Crashlytics.log("logout api at my account screen");
                Crashlytics.logException(t);
                logout();
            }
        });
    }
    private void showLogoutProgress(){
        //AppUtils.showProgressBar(binding.pb);
    }

    private void hideLogoutProgress(){
        //AppUtils.hideProgressBar(binding.pb);
    }
    public void setSelectIndex(int value){
        binding.navBottom.getMenu().getItem(value).setChecked(true);
        switch (value){
            case 0:
                binding.toolbar.setTitle("Appointments");
                break;
            case 1:
                binding.toolbar.setTitle("Welcome to Fixirman.com");
                break;
            case 2:
                binding.toolbar.setTitle("Notification");
                break;
        }
    }
    public void visibleNavigationView(int visibility){
        binding.navBottom.setVisibility(visibility);
        binding.toolbar.setVisibility(visibility);
    }

    public void logout() {
        sessionManager.logoutUser();
        viewModel.logout();
        Intent i = new Intent(this,PhoneActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateUserDetail(sessionManager.getUser());
    }

    @Override
    protected void onDestroy() {
        sessionManager.removeListener(this);
        super.onDestroy();
    }

    private void updateUserDetail(User user){
        String image = "";
        if(user != null) {
            image = user.getImage();
        }
        if(userImage != null){
            Glide.with(this).load(AppConstants.HOST_URL+image).apply(
                    new RequestOptions().error(R.drawable.default_user))
                    .fitCenter()
                    .placeholder(AppUtils.getImageLoader(this))
                    .into(userImage);
        }
        if(username != null){
            username.setText(user != null ? user.getName() : "Unknown user");
        }
    }
}
