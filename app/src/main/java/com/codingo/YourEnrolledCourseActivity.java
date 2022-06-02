package com.codingo;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.bd.R;
import com.codingo.utils.ExpandableHeightGridView;

public class YourEnrolledCourseActivity extends AppCompatActivity {

    ExpandableHeightGridView gridview;

    LinearLayout closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enrolled_course);
        closeIV=findViewById(R.id.closeIV);
        gridview=findViewById(R.id.gridview);
        EnrolledCourseAdapter courseAdapter = new EnrolledCourseAdapter(this,false);
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