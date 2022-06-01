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

import com.codingo.adapter.LessonAdopter;
import com.codingo.bd.R;
import com.codingo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseLessonActivity extends AppCompatActivity {

    TextView titleTV;
    ImageView imageIV;
    TextView descriptionTV;
    LinearLayout closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_lesson);
        titleTV = findViewById(R.id.titleTV);
        imageIV = findViewById(R.id.imageIV);
        closeIV = findViewById(R.id.closeIV);
        descriptionTV = findViewById(R.id.descriptionTV);
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(Utils.getStringValue(getIntent(), "id"))) {
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


    }


}