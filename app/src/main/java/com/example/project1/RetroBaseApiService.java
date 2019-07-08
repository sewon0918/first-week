package com.example.project1;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetroBaseApiService {
    final String Base_URL = "http://ec2-13-209-21-20.ap-northeast-2.compute.amazonaws.com:8000";

    //GET ALL CONTACTS
    @GET("/api/contacts/{id}")
    Call<List<PersonInfo>> getAllContact(@Path("id") String id);

    // ADD SINGLE CONTACT
    @POST("/api/contacts")
    Call<PersonInfo> addContact(@Body PersonInfo personinfo);

    // DELETE CONTACT
    @DELETE("/api/contacts/{id}/{name}")
    Call<ResponseBody> deleteContact(@Path("id") String id, @Path("name") String name);


    //GET ALL GALLERIES
    @GET("/api/galleries/{id}")
    Call<List<GalleryInfo>> getAllGallery(@Path("id") String id);

    // ADD SINGLE GALLERY
    @POST("/api/galleries")
    Call<GalleryInfo> addGallery(@Body GalleryInfo galleryinfo);

    // DELETE GALLERY
    @DELETE("/api/galleries/{id}/{name}")
    Call<ResponseBody> deleteGallery(@Path("id") String id, @Path("name") String name);


    //GET CURRENT BOARD
    @GET("/api/omok/")
    Call<List<coordinates>> getBoard();

    // ADD SINGLE POINT
    @POST("/api/omok")
    Call<coordinates> addPoint(@Body coordinates coordinates);

    // DELETE BOARD
    @DELETE("/api/omok/")
    Call<ResponseBody> deleteBoard();


    // ADD USER
    @POST("/api/user/{id}")
    Call<String> addUser(@Path("id") String id);

    // DELETE USER
    @DELETE("/api/user/{id}")
    Call<ResponseBody> delUser(@Path("id") String id);

    // GET USER
    @GET("/api/user/{id}")
    Call<List<String>> getUser(@Path("id") String id);
}

