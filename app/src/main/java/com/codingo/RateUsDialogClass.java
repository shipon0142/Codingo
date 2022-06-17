package com.codingo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.codingo.bd.BuildConfig;
import com.codingo.bd.R;

public class RateUsDialogClass extends Dialog {
    ImageView imageIV;
    LinearLayout enrollLL;
    LinearLayout cancel;
    int id;
    public interface EnrollCallback{
        public void enrollCallback(String id);
    }
    public RateUsDialogClass(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rate_us);
        imageIV=findViewById(R.id.imageIV);
        enrollLL=findViewById(R.id.enrollLL);
        cancel=findViewById(R.id.cancel);
        enrollLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(
                            "market://details?id="+ BuildConfig.APPLICATION_ID)));
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
