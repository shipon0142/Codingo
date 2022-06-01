package com.codingo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.codingo.bd.R;

public class CourseEnrollDialogClass  extends Dialog {
    ImageView imageIV;
    LinearLayout enrollLL;
    int id;
    public interface EnrollCallback{
        public void enrollCallback(String id);
    }
    public CourseEnrollDialogClass(@NonNull Context context,String id, Integer image,EnrollCallback enrollCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.course_enroll_dialog);
        imageIV=findViewById(R.id.imageIV);
        enrollLL=findViewById(R.id.enrollLL);
        imageIV.setImageResource(image);
        enrollLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollCallback.enrollCallback(id);
                dismiss();
            }
        });

    }
}
