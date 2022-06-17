package com.codingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingo.adapter.LessonAdopter;
import com.codingo.bd.R;
import com.codingo.model.Course;
import com.codingo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class MarksActivity extends AppCompatActivity {


    TextView marksTV;
    TextView outOfTV;
    LinearLayout closeIV;

    LinearLayout continueLL;
    String marks = "0";
    String questions = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        closeIV=findViewById(R.id.closeIV);
        continueLL=findViewById(R.id.continueLL);


        continueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }



}