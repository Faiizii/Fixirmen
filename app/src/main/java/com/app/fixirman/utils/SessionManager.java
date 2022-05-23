package com.app.fixirman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.Gson;

import java.util.List;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared diab_preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREF_NAME = "my_pref_101";

    private final String IS_LOGIN = "is_login";
    private final String FIREBASE_TOKEN = "firebase_token";


    private final String USER_OBJECT = "user_object";
    private final String USER_PHONE = "user_phone";
    private final String USER_BACKUP_PHONE = "user_back_phone";
    private final String USER_PASSWORD = "user_password";

    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";
    private final String USER_IMAGE = "user_image";
    private final String USER_EMAIL = "user_email";



    private final String FIREBASE_LAST_KEY = "firebase_message_key";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        pref.registerOnSharedPreferenceChangeListener(listener);
    }
    public void removeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setIsLogin(boolean value){
        editor.putBoolean(IS_LOGIN,value);
        editor.apply();
    }
    public boolean isLogin(){
        return pref.getBoolean(IS_LOGIN,false);
    }


    public void saveToken(String token) {
        editor.putString(FIREBASE_TOKEN,token);
        editor.apply();
    }
    public String getToken(){
        return pref.getString(FIREBASE_TOKEN,"");
    }

    public User getUser(){
        String userObj = pref.getString(USER_OBJECT, "");
        if(userObj.isEmpty()){
            return null;
        }else{
            return new Gson().fromJson(userObj,User.class);
        }
    }
    public void saveUser(User user){
        editor.putInt(USER_ID,user.getId());
        editor.putString(USER_IMAGE,user.getImage());
        editor.putString(USER_NAME,user.getName());
        editor.putString(USER_EMAIL,user.getEmail());
        editor.putString(USER_PHONE,user.getPhone());
        editor.putString(USER_BACKUP_PHONE,user.getBackupPhone());
        editor.putString(USER_OBJECT,new Gson().toJson(user));
        setIsLogin(true);
        editor.apply();
    }
    public void saveUserLogin(String phone,String password){
        editor.putString(USER_PHONE,phone);
        editor.putString(USER_PASSWORD,password);
        setIsLogin(true);
        editor.apply();
    }
    public String getPhone(){
        return pref.getString(USER_PHONE,"");
    }
    public String getPassword(){
        return pref.getString(USER_PASSWORD,"onlyUsePhone");
    }

    public int getUserId() {
        return pref.getInt(USER_ID,0);
    }
    public String getUserName(){
        return pref.getString(USER_NAME,"");
    }
    public String getUserImage(){
        return pref.getString(USER_IMAGE,"");
    }

    public String getLastFetchedKey() {
        return pref.getString(FIREBASE_LAST_KEY,"");
    }
    public void saveLastFetchedKey(String messageKey){
        editor.putString(FIREBASE_LAST_KEY,messageKey);
        editor.apply();
    }

    public void logoutUser() {
        editor.remove(USER_ID);
        editor.remove(USER_IMAGE);
        editor.remove(USER_NAME);
        editor.remove(USER_EMAIL);
        editor.remove(USER_OBJECT);
        editor.putBoolean(IS_LOGIN,false);
        editor.apply();
    }
}
