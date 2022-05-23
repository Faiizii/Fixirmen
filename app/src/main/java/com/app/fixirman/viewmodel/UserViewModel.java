package com.app.fixirman.viewmodel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.user.AdModel;
import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserAddress;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
public class UserViewModel extends ViewModel {

    private DaoHelper daoHelper;
    @Inject
    public UserViewModel(DaoHelper helper){
        daoHelper = helper;
    }
    public LiveData<List<User>> getAllUsers(int userId, int count){
        return daoHelper.getAllUsers(userId,count);
    }

    //test function
    public void updateUser(User u) {
        daoHelper.updateUser(u);
    }

    public void registerFirebaseListener(int userId) {
        daoHelper.registerFirebaseListeners(userId);
    }
    public LiveData<Integer> getUnreadCount() {
        return daoHelper.getUnreadNotificationCount();
    }
    public void logout() {
        daoHelper.logout();
    }

    public void insertAddress(List<UserAddress> userAddress) {
        daoHelper.insertAddresses(userAddress);
    }

    public void insertAds(List<AdModel> ads) {
        daoHelper.insertAds(ads);
    }

    public LiveData<List<UserAddress>> getAddresses(int userId) {
        return daoHelper.getUserAddresses(userId);
    }

    public void clearUserAddresses(int userId) {
        daoHelper.clearUserAddresses(userId);
    }

    public void deleteAddress(int itemId) {
        daoHelper.deleteAddress(itemId);
    }
}
