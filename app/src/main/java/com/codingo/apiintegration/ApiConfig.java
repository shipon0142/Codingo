package com.codingo.apiintegration;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiConfig {
    @GET()
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })
    Call<ResponseBody> getRequestRetrofit(@Url String url, @QueryMap Map<String, String> queries);
    @POST()
    @FormUrlEncoded
    @Headers({
            "Accept: application/json"
    })
    Call<ResponseBody> getRequestRetrofit(@FieldMap() Map<String,String> MAP, @Url String url);

    @POST()
    @Headers({
            "Accept: application/json"
    })
    Call<ResponseBody> getRequestRetrofit(@Body JSONObject jsonObject, @Url String url);

    @POST()
    @FormUrlEncoded
    @Headers({
            "Accept: application/json"
    })
    Call<ResponseBody> getRequestRetrofit(@Field("customer_signup_name") String customer_signup_name,
                                          @Field("customer_signup_email") String customer_signup_email,
                                          @Field("customer_signup_mobile") String customer_signup_mobile,
                                          @Field("customer_signup_photo_id") String customer_signup_photo_id);
}
