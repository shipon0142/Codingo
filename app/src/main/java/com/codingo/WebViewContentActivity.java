package com.codingo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codingo.bd.R;
import com.codingo.utils.Utils;

public class WebViewContentActivity extends AppCompatActivity {

    TextView titleTV;

    TextView descriptionTV;
    LinearLayout closeIV;
    WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_content);
        titleTV = findViewById(R.id.titleTV);
        webView = findViewById(R.id.webView);
        closeIV = findViewById(R.id.closeIV);
        descriptionTV = findViewById(R.id.descriptionTV);

        titleTV.setText(Utils.getStringValue(getIntent(), "title"));
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadUrl(Utils.getStringValue(getIntent(), "content"));


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
        webView.loadUrl(url);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

}