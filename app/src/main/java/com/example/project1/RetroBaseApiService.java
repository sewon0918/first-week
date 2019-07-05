package com.example.project1;

import java.util.List;

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

    //GET ALL CONTACTS
    @GET("/api/contacts/{id}")
    Call<List<PersonInfo>> getAllContact(@Path("id") String id);


    // ADD SINGLE CONTACT
    @POST("/api/contacts")
    Call<PersonInfo> addContact(@Body PersonInfo personinfo);

    @PUT("/posts/1") Call<ResponseGet> putFirst(@Body RequestPut parameters);


    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();

    // GET SINGLE CONTACT
    @GET("/api/contacts/{Id}")
    Call<PersonInfo> getContact(@Path("Id") String id);

}

