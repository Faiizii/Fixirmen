package com.app.fixirman.api.http;

import com.app.fixirman.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiUtils {

    private static Retrofit retrofit = null;
    public static ApiInterface getApiInterface() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppConstants.HOST_URL)
                    .client(
                            new OkHttpClient.Builder()
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .build()
                    ).build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
