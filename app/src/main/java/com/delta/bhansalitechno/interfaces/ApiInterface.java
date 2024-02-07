package com.delta.bhansalitechno.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


public interface ApiInterface {

    @Multipart
    @POST
    Call<ResponseBody> getUrlMap(
            @Url String url,
            @PartMap Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST
    Call<ResponseBody> getUrlMap(
            @Url String url,
            @PartMap Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file1,@Part MultipartBody.Part file2
    );

    @GET
    Call<ResponseBody> getUrlMap(@Url String url);
}
