package com.codingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.utils.Utils;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView titleTV;
    ImageView imageIV;
    TextView descriptionTV;
    LinearLayout closeIV;
    LinearLayout enrollLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        titleTV = findViewById(R.id.titleTV);
        imageIV = findViewById(R.id.imageIV);
        closeIV = findViewById(R.id.closeIV);
        enrollLL = findViewById(R.id.enrollLL);
        descriptionTV = findViewById(R.id.descriptionTV);
        Integer image=R.drawable.python;
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(Utils.getStringValue(getIntent(), "id"))) {
                image=MainActivity.courses.get(i).getImage();
                imageIV.setImageResource(MainActivity.courses.get(i).getImage());
                break;
            }
        }
        titleTV.setText(Utils.getStringValue(getIntent(), "title"));
        descriptionTV.setText(Utils.getStringValue(getIntent(), "description"));
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Integer finalImage = image;
        enrollLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mycourses[] = MainActivity.LOGGED_IN_USER.getEnrolled().split(",");
                for (int i = 0; i < mycourses.length; i++) {
                    if (mycourses[i].equals("course" + Utils.getStringValue(getIntent(), "id"))) {
                        Toast.makeText(CourseDetailsActivity.this, "Already Enrolled", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                CourseEnrollDialogClass cdd = new CourseEnrollDialogClass(CourseDetailsActivity.this, Utils.getStringValue(getIntent(), "id"), finalImage, new CourseEnrollDialogClass.EnrollCallback() {
                    @Override
                    public void enrollCallback(String id) {
                        new FirebaseManager(CourseDetailsActivity.this).addEnrolledCourse(MainActivity.LOGGED_IN_USER.getId(), MainActivity.LOGGED_IN_USER.getEnrolled() + "course" + id + ",", new FirebaseManager.SuccessListener() {
                            @Override
                            public void getsuccess() {
                                Intent i = new Intent(CourseDetailsActivity.this, CourseDescriptionActivity.class);
                                i.putExtra("title", Utils.getStringValue(getIntent(), "title"));
                                i.putExtra("id", id);
                                startActivity(i);
                                finish();





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

 }
 private void showDialog(String title,String id){
     Intent i = new Intent(CourseDetailsActivity.this, CourseDescriptionActivity.class);
     i.putExtra("title", title);
     i.putExtra("id", id);
     startActivity(i);
     finish();
     SuccessfullyEnrolled cdd2 = new SuccessfullyEnrolled(CourseDetailsActivity.this, new SuccessfullyEnrolled.EnrollCallback() {
         @Override
         public void enrollCallback(String id) {
          /*   */
         }
     });
     cdd2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
     cdd2.show();
 }


}