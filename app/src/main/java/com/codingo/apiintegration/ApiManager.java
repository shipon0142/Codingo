package com.codingo.apiintegration;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ApiManager {

    public Map<String, RequestBody> getBody(Map<String,String> map){
        Map<String, RequestBody> bodyMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) {


                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), entry.getValue());
                bodyMap.put(entry.getKey(), description);

            }
        }
        return bodyMap;



    }

    public void getRequestRetrofit(Context context, String url, Map<String, String> queries, int requestMethod, Callback<ResponseBody> callback) {
       // Map<String, String> params = new Util().getCommonUserParams(context, queries);
        Map<String, String> params = new HashMap<>();
        ApiConfig getResponse = Util.getApiClient(context).create(ApiConfig.class);
        Call<ResponseBody> responseCall;
        if (requestMethod == Configuration.GET_REQUEST)
            responseCall = getResponse.getRequestRetrofit( url, params);
        else responseCall = getResponse.getRequestRetrofit(queries, url);
        responseCall.enqueue(callback);
    }    public void getRequestRetrofit(Context context, String url, JSONObject jsonObject, int requestMethod, Callback<ResponseBody> callback) {
       // Map<String, String> params = new Util().getCommonUserParams(context, queries);
        Map<String, String> params = new HashMap<>();
        ApiConfig getResponse = Util.getApiClient(context).create(ApiConfig.class);
        Call<ResponseBody> responseCall;
        if (requestMethod == Configuration.GET_REQUEST)
            responseCall = getResponse.getRequestRetrofit( url, params);
        else responseCall = getResponse.getRequestRetrofit(jsonObject, url);
        responseCall.enqueue(callback);
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public interface JsonObjectCallBack {
        public void getJsonObject(int ResponseCode, JSONObject jsonObject);
    }


    public void getRequestRetrofit(final Context context, final String url, final Map<String, String> queries, final boolean checkCache, final int requestMethod, final JsonObjectCallBack jsonObjectCallBack) {

        getRequestRetrofit(context, url, queries, requestMethod, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String body = "";
                JSONObject jsonObject = new JSONObject();
                if (response.code() == 200) {
                    try {
                        body = response.body().string();
                        String json = new String(body);
                        if (!isJSONValid(json.trim())) {
                            Log.d("appApiRequest", json.trim());
                            jsonObjectCallBack.getJsonObject(0, jsonObject);
                            return;
                        }
                        if (new JsonParser().parse(json.trim()).isJsonObject()) {
                            //if (checkCache)
                              //  CacheData.setCacheData(context, url, queries, json.trim());

                            try {
                                jsonObject = new JSONObject(body);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    } catch (IOException e) {
                        body = e.getMessage();
                        e.printStackTrace();
                    }
                    Log.d("appApiRequest", body);
                    jsonObjectCallBack.getJsonObject(response.code(), jsonObject);

                } else {
                    try {
                       if(response!=null && response.errorBody()!=null) body = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }try {
                       if(response!=null && response.body()!=null) body = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("appApiRequest", "response : "+response.code()+body);
                    try {
                        if(!body.equals("")) {

                            try {
                                jsonObject = new JSONObject(body);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if(!url.contains("air-order-placement")) {

                    }
                    jsonObjectCallBack.getJsonObject(response.code(), jsonObject);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String mess = "";
                try {
                    mess = t.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("appApiRequest", mess);
                JSONObject jsonObject = new JSONObject();
               // jsonObjectCallBack.getJsonObject(0, jsonObject);

            }
        });
    }


    public String getMyRequestUrl(Map<String, String> stringMap) {


        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            if (entry.getValue() != null) {
                if (str.toString().equals(""))
                    str.append(entry.getKey()).append("=").append(entry.getValue().toString());
                else
                    str.append("&").append(entry.getKey()).append("=").append(entry.getValue().toString());
            }
        }

        return str.toString();
    }







}
