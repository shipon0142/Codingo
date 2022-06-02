package com.codingo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.model.Course;
import com.codingo.model.User;
import com.codingo.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout tabDots;
    ViewPager pager;
   public static SharedPreferences sharedPreferences;
   public static JSONObject PUBLIC_JSON_OBJECT;
    // Creating an Editor object to edit(write to the file)
    public static SharedPreferences.Editor myEdit;
    public static User LOGGED_IN_USER;
    public  static ArrayList<Course>courses=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        courses=new ArrayList<>();
        courses.clear();
       courses.add(new Course("1","Java",R.drawable.java));
        courses.add(new Course("2","C++",R.drawable.c_plus_plus));
        courses.add(new Course("3","Python",R.drawable.python));
        courses.add(new Course("4","C#",R.drawable.c_sharp));
        courses.add(new Course("5","Php",R.drawable.php));
        courses.add(new Course("6","Git",R.drawable.git));
        try {
           FirebaseAuth.getInstance().signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new FirebaseManager(this).getMyUserInfo(LOGGED_IN_USER.getEmail(), new FirebaseManager.RetriveUserListener() {
            @Override
            public void getUser(User user) {
                LOGGED_IN_USER=user;
                setAll();
            }
        });

    }
    private void setAll(){
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        tabDots = findViewById(R.id.tabDots);
        pager = (ViewPager) findViewById(R.id.pager);

        tabDots.setupWithViewPager(pager, true);

        tabDots.addTab(tabDots.newTab().setText("Tab1"));
        tabDots.addTab(tabDots.newTab().setText("Tab2"));
        tabDots.addTab(tabDots.newTab().setText("Tab3"));
        tabDots.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), tabDots.getTabCount());
        pager.setAdapter(adapter);
        for (int i = 0; i < tabDots.getTabCount(); i++) {
            TabLayout.Tab tab = tabDots.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 2) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#3F51B5"));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#3F51B5"));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

    }
}