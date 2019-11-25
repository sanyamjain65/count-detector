package com.example.countdetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {

    public boolean hasInternetAccess(Context context) throws IOException {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                context = null;
                return (urlc.getResponseCode() == 204);
            } catch (IOException e) {
//                Log.e(this.getClass().getSimpleName(), "Error checking internet connection", e);
            }
        }
        context = null;
        return false;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public void setInPersistenceStorage(Context context,String key, int count) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, count);
        editor.commit();
        editor.apply();
    }

    public void makeServerCall(Context context,String key, int count, final StatusInterface statusInterface) {
        if(isNetworkAvailable(context)) {
            ApiInterface apiService = RetrofitClient.getClient().create(ApiInterface.class);
            final RetrofitRequest retrofitRequest = new RetrofitRequest("small", count);
            Call<ResponseBody> responseCall = apiService.sendCount(retrofitRequest);
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200 || response.code() == 201) {
                        statusInterface.onSuccess();

                    } else {
                        statusInterface.onFailure(response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    statusInterface.onFailure(1000);;

                }
            });
        } else {
            statusInterface.onFailure(2000);
        }

    }

    public int getSharePreference(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        int count = sharedPref.getInt(key, 0);
        return count;
    }
}
