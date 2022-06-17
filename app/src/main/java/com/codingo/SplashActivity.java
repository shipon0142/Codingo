package com.codingo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.codingo.apiintegration.ApiManager;
import com.codingo.apiintegration.Configuration;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = new User();
        MainActivity.sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String u = MainActivity.sharedPreferences.getString("user", "");
        if (!u.equals("")) {
            user = new Gson().fromJson(u, User.class);
        }
        FirebaseApp.initializeApp(this);
        User finalUser = user;
        final Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)


                    if (!finalUser.getEmail().equals("")) {

                        new FirebaseManager(SplashActivity.this).getMyUserInfo(finalUser.getEmail(), new FirebaseManager.RetriveUserListener() {
                            @Override
                            public void getUser(User user) {
                                com.codingo.MainActivity.LOGGED_IN_USER = user;

                                MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                                MainActivity.myEdit.putString("user", new Gson().toJson(user, User.class));
                                MainActivity.myEdit.apply();

                                if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                                    new ApiManager().getRequestRetrofit(SplashActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                                        @Override
                                        public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                            MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                            Intent openstartingpoint = new Intent(SplashActivity.this, com.codingo.MainActivity.class);
                                            startActivity(openstartingpoint);
                                            finish();

                                        }
                                    });
                                }else {
                                    Intent openstartingpoint = new Intent(SplashActivity.this, com.codingo.MainActivity.class);
                                    startActivity(openstartingpoint);
                                    finish();
                                }

                            }
                        });

                    } else {
                        Intent openstartingpoint = new Intent(SplashActivity.this, com.codingo.LoginActivity.class);
                        openstartingpoint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(openstartingpoint);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }
}