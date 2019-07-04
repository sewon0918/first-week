package com.example.project1;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroBaseApiService {
    final String Base_URL = "http://143.248.36.12:8080";

    // GET ALL CONTACTS
//    @GET("/{user_id}")    Call<List<PersonInfo>> getName;
//    @Query("Id") String id);

    // GET SINGLE CONTACT
    @GET("/api/contacts/{Id}")
    Call<PersonInfo> getName(@Path("Id") String id);

    // ADD SINGLE CONTACT
    @POST("/api/contacts")
    Call<PersonInfo> addContact(@Body PersonInfo personinfo);

    @PUT("/posts/1") Call<ResponseGet> putFirst(@Body RequestPut parameters);


    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();

}

