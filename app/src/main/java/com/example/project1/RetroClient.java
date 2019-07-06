package com.example.project1;

import android.content.Context;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class RetroClient {

    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetroClient createBaseApi() {
        apiService = create(RetroBaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void getAllContact(String id, final RetroCallback callback) {
        apiService.getAllContact(id).enqueue(new Callback<List<PersonInfo>>(){
            @Override
            public void onResponse(Call<List<PersonInfo>> call, Response<List<PersonInfo>> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PersonInfo>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void addContact(PersonInfo personInfo, final RetroCallback callback){
        apiService.addContact(personInfo).enqueue((new Callback<PersonInfo>() {
            @Override
            public void onResponse(Call<PersonInfo> call, Response<PersonInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<PersonInfo> call, Throwable t) {
                callback.onError(t);
            }
        }));
    }

    public void deleteContact(String id, String name, final RetroCallback callback) {
        apiService.deleteContact(id, name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getAllGallery(String id, final RetroCallback callback) {
        apiService.getAllGallery(id).enqueue(new Callback<List<GalleryInfo>>(){
            @Override
            public void onResponse(Call<List<GalleryInfo>> call, Response<List<GalleryInfo>> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<List<GalleryInfo>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void addGallery(GalleryInfo galleryInfo, final RetroCallback callback){
        apiService.addGallery(galleryInfo).enqueue((new Callback<GalleryInfo>() {
            @Override
            public void onResponse(Call<GalleryInfo> call, Response<GalleryInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<GalleryInfo> call, Throwable t) {
                callback.onError(t);
            }
        }));
    }

    public void deleteGallery(String id, String name, final RetroCallback callback) {
        apiService.deleteGallery(id, name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
