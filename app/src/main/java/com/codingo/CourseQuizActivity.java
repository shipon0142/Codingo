package com.codingo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseQuizActivity extends AppCompatActivity {
    int currentPage = 0;
    JSONObject jsonObject = new JSONObject();
    JSONArray questions = new JSONArray();
    LinearLayout ind1, ind2, ind3, ind4, ind5, ind6, ind7;
    TextView optionATV;
    TextView optionBTV;
    TextView optionCTV;
    TextView optionDTV;
    TextView filloptionATV;
    TextView filloptionBTV;
    TextView filloptionCTV;
    TextView filloptionDTV;
    TextView questionTV;
    CardView cv1;
    CardView cv2;
    CardView cv3;
    CardView cv4;

    CardView fillcv1;
    CardView fillcv2;
    CardView fillcv3;
    CardView fillcv4;
    String answer = "";
    String ANS_A = "a,";
    String ANS_B = "b,";
    LinearLayout fillIntheTitleblankLL;
    String ANS_C = "c,";
    String ANS_D = "d,";
    LinearLayout continueLL;
    LinearLayout fillIntheBlankOptionLL;
    LinearLayout mcqMainLL;
    TextView titleFillIntheBlankTV;
    TextView fillInTheblankAnsTV;
    TextView titleTV;
    LinearLayout closeIV;
    int marks = 0;
    HashMap<String, String> result = new HashMap<>();
    ArrayList<LinearLayout> indicator = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_quiz);
        indicator = new ArrayList<>();
        result = new HashMap<>();
        continueLL = findViewById(R.id.continueLL);
        fillIntheTitleblankLL = findViewById(R.id.fillIntheTitleblankLL);
        fillIntheBlankOptionLL = findViewById(R.id.fillIntheBlankOptionLL);
        mcqMainLL = findViewById(R.id.mcqMainLL);
        fillInTheblankAnsTV = findViewById(R.id.fillInTheblankAnsTV);
        titleTV = findViewById(R.id.titleTV);

        titleFillIntheBlankTV = findViewById(R.id.titleFillIntheBlankTV);
        questionTV = findViewById(R.id.questionTV);
        closeIV = findViewById(R.id.closeIV);
        marks = 0;
        titleTV.setText(Utils.getStringValue(getIntent(), "title"));
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ind1 = findViewById(R.id.ind1);
        ind2 = findViewById(R.id.ind2);
        ind3 = findViewById(R.id.ind3);
        ind4 = findViewById(R.id.ind4);
        ind5 = findViewById(R.id.ind5);
        ind6 = findViewById(R.id.ind6);
        ind7 = findViewById(R.id.ind7);
        indicator.add(ind1);
        indicator.add(ind2);
        indicator.add(ind3);
        indicator.add(ind4);
        indicator.add(ind5);
        indicator.add(ind6);
        indicator.add(ind7);

        optionATV = findViewById(R.id.optionATV);
        optionBTV = findViewById(R.id.optionBTV);
        optionCTV = findViewById(R.id.optionCTV);
        optionDTV = findViewById(R.id.optionDTV);

        filloptionATV = findViewById(R.id.filloptionATV);
        filloptionBTV = findViewById(R.id.filloptionBTV);
        filloptionCTV = findViewById(R.id.filloptionCTV);
        filloptionDTV = findViewById(R.id.filloptionDTV);
        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);

        fillcv1 = findViewById(R.id.fillcv1);
        fillcv2 = findViewById(R.id.fillcv2);
        fillcv3 = findViewById(R.id.fillcv3);
        fillcv4 = findViewById(R.id.fillcv4);

        jsonObject = MainActivity.PUBLIC_JSON_OBJECT;

        questions = Utils.getJsonArray(Utils.getJsonObject(jsonObject, "id" + Utils.getStringValue(getIntent(), "id")), "questions");
        currentPage = 0;
        setIndicator();
        clearMcq();
        clearFillinTheGap();
        if (Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "type").equals("1")) {
            setoptions();
        }
        if (Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "type").equals("2")) {
            setoptionsForFillIntheBlank();
        } else {

        }


        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_A, cv1, optionATV);
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_B, cv2, optionBTV);
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_C, cv3, optionCTV);
            }
        });
        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_D, cv4, optionDTV);
            }
        });

        fillcv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFillinTheGap();
                setMarkResult(ANS_A, fillcv1, filloptionATV);
                if (answer.equals("")) {
                    fillInTheblankAnsTV.setText("");
                } else {
                    fillInTheblankAnsTV.setText(filloptionATV.getText().toString());
                }
            }
        });
        fillcv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFillinTheGap();
                setMarkResult(ANS_B, fillcv2, filloptionBTV);
                if (answer.equals("")) {
                    fillInTheblankAnsTV.setText("");
                    fillInTheblankAnsTV.setPaintFlags(0);
                } else {
                    fillInTheblankAnsTV.setText(filloptionBTV.getText().toString());
                }
            }
        });
        fillcv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFillinTheGap();
                setMarkResult(ANS_C, fillcv3, filloptionCTV);
                if (answer.equals("")) {
                    fillInTheblankAnsTV.setText("");
                    fillInTheblankAnsTV.setPaintFlags(0);
                } else {
                    fillInTheblankAnsTV.setText(filloptionCTV.getText().toString());
                }
            }
        });
        fillcv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFillinTheGap();
                setMarkResult(ANS_D, fillcv4, filloptionDTV);
                if (answer.equals("")) {
                    fillInTheblankAnsTV.setText("");
                    fillInTheblankAnsTV.setPaintFlags(0);
                } else {
                    fillInTheblankAnsTV.setText(filloptionDTV.getText().toString());
                }

            }
        });

        continueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer.equals("")) {
                    Toast.makeText(CourseQuizActivity.this, "Please select a answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean flag = false;
                if (answer.contains(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "answer")) && answer.length() == Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "answer").length()) {
                    flag = true;
                    marks++;
                } else {
                    flag = false;
                }
                showResult(flag);
                clearMcq();
                clearFillinTheGap();

                if ((questions.length() - 1) > currentPage) {
                    currentPage++;
                    setIndicator();
                    if (Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "type").equals("1")) {
                        setoptions();
                    }
                    if (Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "type").equals("2")) {
                        setoptionsForFillIntheBlank();
                    } else {

                    }
                } else {

                    Intent i=new Intent(CourseQuizActivity.this,MarksActivity.class);
                    i.putExtra("marks",""+marks);
                    i.putExtra("questions",""+questions.length());
                    startActivity(i);
                    finish();

                }
            }
        });


    }

    private void showResult(boolean flag) {


        ResultDialogClass cdd = new ResultDialogClass(this, flag);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();


    }

    private void clearMcq() {
        clearAll(ANS_A, cv1, optionATV);
        clearAll(ANS_B, cv2, optionBTV);
        clearAll(ANS_C, cv3, optionCTV);
        clearAll(ANS_D, cv4, optionDTV);
    }

    private void clearFillinTheGap() {
        clearAll(ANS_A, fillcv1, filloptionATV);
        clearAll(ANS_B, fillcv2, filloptionBTV);
        clearAll(ANS_C, fillcv3, filloptionCTV);
        clearAll(ANS_D, fillcv4, filloptionDTV);
        fillInTheblankAnsTV.setText("");
    }

    private void setIndicator() {
        for (int i = 0; i < indicator.size(); i++) {
            if (i <= currentPage) indicator.get(i).setVisibility(View.INVISIBLE);
            else indicator.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void setMarkResult(String ANS, CardView cardView, TextView optionTV) {
        if (answer.contains(ANS)) {
            cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            optionTV.setBackgroundColor(Color.parseColor("#ffffff"));
            optionTV.setTextColor(Color.parseColor("#000000"));
            answer = answer.replace(ANS, "");
        } else {
            cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#7433FF")));
            optionTV.setBackgroundColor(Color.parseColor("#7433FF"));
            optionTV.setTextColor(Color.parseColor("#ffffff"));
            answer = answer + ANS;
        }
    }

    private void clearAll(String ANS, CardView cardView, TextView optionTV) {

        cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        optionTV.setBackgroundColor(Color.parseColor("#ffffff"));
        optionTV.setTextColor(Color.parseColor("#000000"));
        answer = "";

    }

    private void setoptions() {
        fillIntheTitleblankLL.setVisibility(View.GONE);
        questionTV.setVisibility(View.VISIBLE);
        fillIntheBlankOptionLL.setVisibility(View.GONE);
        mcqMainLL.setVisibility(View.VISIBLE);
        questionTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "question"));
        optionATV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "a"));
        optionBTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "b"));
        if (!Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "c").equals("")) {
            optionCTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "c"));
            optionDTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "d"));
            cv3.setVisibility(View.VISIBLE);
            cv4.setVisibility(View.VISIBLE);
        } else {
            cv3.setVisibility(View.INVISIBLE);
            cv4.setVisibility(View.INVISIBLE);
        }
    }

    private void setoptionsForFillIntheBlank() {
        fillIntheTitleblankLL.setVisibility(View.VISIBLE);
        questionTV.setVisibility(View.GONE);
        fillIntheBlankOptionLL.setVisibility(View.VISIBLE);
        mcqMainLL.setVisibility(View.GONE);


        fillInTheblankAnsTV.setText("");
        titleFillIntheBlankTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "question"));
        filloptionATV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "a"));
        filloptionBTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "b"));
        filloptionCTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "c"));
        filloptionDTV.setText(Utils.getStringValue(Utils.getJsonObject(questions, currentPage), "d"));
    }


}