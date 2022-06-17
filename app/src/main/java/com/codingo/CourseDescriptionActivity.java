package com.codingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.codingo.adapter.LessonAdopter;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.model.Course;
import com.codingo.model.User;
import com.codingo.utils.Utils;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseDescriptionActivity extends AppCompatActivity {

    RecyclerView recycler;
    TextView titleTV;
    TextView lessonsTV;
    ImageView imageIV;
    LinearLayout closeIV;
    LinearLayout continueLL;
    RoundCornerProgressBar pbarLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_description);
        recycler = findViewById(R.id.recycler);
        titleTV = findViewById(R.id.titleTV);
        imageIV = findViewById(R.id.imageIV);
        lessonsTV = findViewById(R.id.lessonsTV);
        pbarLoading = findViewById(R.id.pbarLoading);
        pbarLoading.setScaleY(1.5f);
        closeIV = findViewById(R.id.closeIV);
        continueLL = findViewById(R.id.continueLL);
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(Utils.getStringValue(getIntent(), "id"))) {
                imageIV.setImageResource(MainActivity.courses.get(i).getImage());
                break;
            }
        }


        titleTV.setText(Utils.getStringValue(getIntent(), "title"));
        JSONObject jsonObject = new JSONObject();
        jsonObject = MainActivity.PUBLIC_JSON_OBJECT;

        JSONArray lessons = Utils.getJsonArray(Utils.getJsonObject(jsonObject, "id" + Utils.getStringValue(getIntent(), "id").toLowerCase()), "lesson");
        lessonsTV.setText(lessons.length()+" lessons");
        LessonAdopter indicatorAdopter = new LessonAdopter(this, lessons, Utils.getStringValue(getIntent(), "id"));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(indicatorAdopter);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        continueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CourseDescriptionActivity.this, CourseQuizActivity.class);
                i.putExtra("title", getCourse(Utils.getStringValue(getIntent(), "id")).getName());
                i.putExtra("id", getCourse(Utils.getStringValue(getIntent(), "id")).getId());
                startActivity(i);
            }
        });

    }
    private Course getCourse(String id) {
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(id)) {
                return MainActivity.courses.get(i);
            }
        }
        return new Course();
    }



}