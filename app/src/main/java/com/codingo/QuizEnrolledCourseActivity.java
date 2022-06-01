package com.codingo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.adapter.LessonAdopter;
import com.codingo.bd.R;
import com.codingo.utils.ExpandableHeightGridView;
import com.codingo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizEnrolledCourseActivity extends AppCompatActivity {

    ExpandableHeightGridView gridview;

    LinearLayout closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_enrolled_course);
        closeIV=findViewById(R.id.closeIV);
        gridview=findViewById(R.id.gridview);
        EnrolledCourseAdapter courseAdapter = new EnrolledCourseAdapter(this,true);
        gridview.setExpanded(true);
        gridview.setAdapter(courseAdapter);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


}