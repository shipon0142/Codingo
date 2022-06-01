package com.codingo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codingo.bd.R;
import com.codingo.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CourseQuizActivity extends AppCompatActivity {
   int currentPage=0;
    JSONObject jsonObject=new JSONObject();
    JSONArray questions=new JSONArray();
    TextView optionATV;
    TextView optionBTV;
    TextView optionCTV;
    TextView optionDTV;
    TextView questionTV;
    CardView cv1;
    CardView cv2;
    CardView cv3;
    CardView cv4;
    String answer="";
    String ANS_A="a,";
    String ANS_B="b,";
    String ANS_C="c,";
    String ANS_D="d,";
    LinearLayout continueLL;
    HashMap<String,String>result=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_quiz);
        result=new HashMap<>();
        continueLL=findViewById(R.id.continueLL);

        questionTV=findViewById(R.id.questionTV);


        optionATV=findViewById(R.id.optionATV);
        optionBTV=findViewById(R.id.optionBTV);
        optionCTV=findViewById(R.id.optionCTV);
        optionDTV=findViewById(R.id.optionDTV);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        cv3=findViewById(R.id.cv3);
        cv4=findViewById(R.id.cv4);
        try {
            jsonObject=new JSONObject(Utils.getFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questions=Utils.getJsonArray(Utils.getJsonObject(jsonObject,"python"),"questions");
        currentPage=0;
        clearAll(ANS_A,cv1,optionATV);
        clearAll(ANS_B,cv2,optionBTV);
        clearAll(ANS_C,cv3,optionCTV);
        clearAll(ANS_D,cv4,optionDTV);
        setoptions();

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_A,cv1,optionATV);
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_B,cv2,optionBTV);
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_C,cv3,optionCTV);
            }
        });
        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMarkResult(ANS_D,cv4,optionDTV);
            }
        });

        continueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll(ANS_A,cv1,optionATV);
                clearAll(ANS_B,cv2,optionBTV);
                clearAll(ANS_C,cv3,optionCTV);
                clearAll(ANS_D,cv4,optionDTV);
                if((questions.length()-1)>currentPage) {
                    currentPage++;
                    setoptions();
                }
            }
        });


    }
    private  void setMarkResult(String ANS,CardView cardView,TextView optionTV){
        if(answer.contains(ANS)){
            cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            optionTV.setBackgroundColor(Color.parseColor("#ffffff"));
            optionTV.setTextColor(Color.parseColor("#000000"));
            answer=answer.replace(ANS,"");
        }else {
            cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#7433FF")));
            optionTV.setBackgroundColor(Color.parseColor("#7433FF"));
            optionTV.setTextColor(Color.parseColor("#ffffff"));
            answer=answer+ANS;
        }
    }
    private  void clearAll(String ANS,CardView cardView,TextView optionTV){

            cardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            optionTV.setBackgroundColor(Color.parseColor("#ffffff"));
            optionTV.setTextColor(Color.parseColor("#000000"));
            answer="";

    }
    private void setoptions() {

        questionTV.setText(Utils.getStringValue(Utils.getJsonObject(questions,currentPage),"question"));
        optionATV.setText(Utils.getStringValue(Utils.getJsonObject(questions,currentPage),"a"));
        optionBTV.setText(Utils.getStringValue(Utils.getJsonObject(questions,currentPage),"b"));
        optionCTV.setText(Utils.getStringValue(Utils.getJsonObject(questions,currentPage),"c"));
        optionDTV.setText(Utils.getStringValue(Utils.getJsonObject(questions,currentPage),"d"));
    }


}