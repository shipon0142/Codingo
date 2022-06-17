package com.codingo.utils;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
/*
    public static String getFromAsset(Context context) {
        StringBuilder json = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("my_res.json"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                json.append(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return json.toString();
    }
*/


    public static JSONObject getJsonObject(JSONObject jsonObject, String field) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1 = jsonObject.getJSONObject(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1;


    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1 = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1;


    }

    public static String getStringValue(JSONObject jsonObject, String field) {
        String str = "";
        try {
            str = jsonObject.getString(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;


    }

    public static String getStringValue(Intent intent, String field) {
        String str = "";
        try {
            str = intent.getStringExtra(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str == null) str = "";
        return str;


    }
    public static int getIntegerValue(Intent intent, String field) {
        int str = 0;
        try {
            str = intent.getIntExtra(field,0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;


    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String field) {
        JSONArray jsonObject1 = new JSONArray();
        try {
            jsonObject1 = jsonObject.getJSONArray(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1;


    }
}
