package com.example.project1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetroBaseApiService {
    final String Base_URL = "http://143.248.36.26:8000";

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

