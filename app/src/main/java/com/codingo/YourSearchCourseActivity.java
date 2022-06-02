package com.codingo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codingo.adapter.CourseAdapter;
import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.utils.ExpandableHeightGridView;
import com.codingo.utils.Utils;

public class YourSearchCourseActivity extends AppCompatActivity {

    ExpandableHeightGridView gridview;

    LinearLayout closeIV;
    String text="";
    TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        closeIV=findViewById(R.id.closeIV);
        titleTV=findViewById(R.id.titleTV);
        gridview=findViewById(R.id.gridview);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text= Utils.getStringValue(getIntent(),"text");
        if(text.equals("")){
            titleTV.setText("All Courses");
        }else {
            titleTV.setText("Your search result");
        }


        CourseAdapter courseAdapter = new CourseAdapter(this,true,text, new CourseAdapter.CourseIdcallback() {
            @Override
            public void getCourse(String id, Integer image) {
                String mycourses[] = MainActivity.LOGGED_IN_USER.getEnrolled().split(",");
                for (int i = 0; i < mycourses.length; i++) {
                    if (mycourses[i].equals("course" + id)) {
                        Toast.makeText(YourSearchCourseActivity.this, "Already Enrolled", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                CourseEnrollDialogClass cdd = new CourseEnrollDialogClass(YourSearchCourseActivity.this, id, image, new CourseEnrollDialogClass.EnrollCallback() {
                    @Override
                    public void enrollCallback(String id) {
                        new FirebaseManager(YourSearchCourseActivity.this).addEnrolledCourse(MainActivity.LOGGED_IN_USER.getId(), MainActivity.LOGGED_IN_USER.getEnrolled() + "course" + id + ",", new FirebaseManager.SuccessListener() {
                            @Override
                            public void getsuccess() {

                                Toast.makeText(YourSearchCourseActivity.this, "Successfully Enrolled", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void getFail() {

                            }
                        });
                    }
                });
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });
        gridview.setExpanded(true);
        gridview.setAdapter(courseAdapter);


    }


}