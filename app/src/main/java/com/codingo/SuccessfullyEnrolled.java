package com.codingo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.codingo.bd.R;

public class SuccessfullyEnrolled extends Dialog {
    ImageView imageIV;
    LinearLayout enrollLL;
    LinearLayout cancel;
    int id;
    public interface EnrollCallback{
        public void enrollCallback(String id);
    }
    public SuccessfullyEnrolled(@NonNull Context context,EnrollCallback enrollCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.successfully_enrolled);
        imageIV=findViewById(R.id.imageIV);
        enrollLL=findViewById(R.id.enrollLL);
        cancel=findViewById(R.id.cancel);
        enrollLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollCallback.enrollCallback("1");
            }
        });


    }
}
