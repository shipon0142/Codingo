package com.codingo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingo.adapter.LessonAdopter;
import com.codingo.bd.R;
import com.codingo.utils.Utils;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseLessonActivity extends AppCompatActivity {

    TextView titleTV;
    ImageView imageIV;
    TextView descriptionTV;
    TextView tapToContinueTV;
    LinearLayout closeIV;
    LinearLayout continueLL;
    WebView webView;
    int index = 0;
    JSONArray descriptions = new JSONArray();
    JSONArray lessons = new JSONArray();
    int lineCount = 0;
    int position = 0;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_lesson);
        titleTV = findViewById(R.id.titleTV);
        tapToContinueTV = findViewById(R.id.tapToContinueTV);
        webView = findViewById(R.id.webView);
        imageIV = findViewById(R.id.imageIV);
        closeIV = findViewById(R.id.closeIV);
        continueLL = findViewById(R.id.continueLL);
        descriptionTV = findViewById(R.id.descriptionTV);
        try {
            lessons = new JSONArray(Utils.getStringValue(getIntent(), "lessons"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int n = 0;
        try {
            n = Integer.parseInt(Utils.getStringValue(getIntent(), "position"));
            String str = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        position = n;
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(Utils.getStringValue(getIntent(), "id"))) {
                imageIV.setImageResource(MainActivity.courses.get(i).getImage());
                break;
            }
        }

        setForPosition(position);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // loadUrl(Utils.getStringValue(getIntent(), "description"));


    }

    private void setForPosition(int p) {
        if (Utils.getStringValue(Utils.getJsonObject(lessons, p), "type").equals("1")) {
            titleTV.setText(Utils.getStringValue(Utils.getJsonObject(lessons, p), "title"));
            try {
                descriptions = new JSONArray(Utils.getStringValue(Utils.getJsonObject(lessons, p), "descriptions"));
            } catch (JSONException e) {
                e.printStackTrace();
                descriptions = new JSONArray();
            }
            index = 0;
            setText(index);
        }
        if (Utils.getStringValue(Utils.getJsonObject(lessons, p), "type").equals("3")) {
            Intent i = new Intent(CourseLessonActivity.this, CourseQuizActivity.class);
            i.putExtra("title", Utils.getStringValue(Utils.getJsonObject(lessons, p), "title"));
            i.putExtra("id", Utils.getStringValue(getIntent(), "id"));
            startActivity(i);
            finish();
        }
    }

    private void setText(int inde) {
        String text = Utils.getStringValue(Utils.getJsonObject(descriptions, inde), "description").replaceAll("\n", "");
        descriptionTV.setText(text);

        final int[] length = {1};
        descriptionTV.setMaxLines(length[0]);
        showButton();
        continueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (descriptionTV.getMaxLines() == lineCount) {
                    if ((index + 1) == descriptions.length()) {
                        finish();
                    }
                    index++;
                    setText(index);

                } else {
                    length[0]++;
                    descriptionTV.setMaxLines(descriptionTV.getMaxLines() + 1);
                    showButton();
                }


            }
        });

    }

    private void showButton() {
        descriptionTV.post(new Runnable() {
            @Override
            public void run() {
                lineCount = descriptionTV.getLineCount();

                if (lineCount <= descriptionTV.getMaxLines()) {
                    continueLL.setBackgroundResource(R.drawable.button_rounded_background2);
                    tapToContinueTV.setTextColor(Color.parseColor("#ffffff"));
                    if (index == descriptions.length() - 1) {
                        //  tapToContinueTV.setText("End");
                        position++;
                        setForPosition(position);
                    }

                } else {
                    continueLL.setBackgroundResource(R.drawable.button_rounded_background3);
                    tapToContinueTV.setTextColor(Color.parseColor("#000000"));
                }
            }
        });


    }

    private int getScale() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(360);
        val = val * 100d;
        return val.intValue();
    }

    private void loadUrl(String url) {
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webView.setPadding(0, 0, 0, 0);
        webView.setInitialScale(getScale());
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUserAgentString("com.travellersguru.bd");
        //  webView.addJavascriptInterface(new WebViewActivity.WebAppInterface(this), "Android");
        webView.loadData(url, "text/html", "UTF-8");
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

}