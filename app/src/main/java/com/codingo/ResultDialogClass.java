package com.codingo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codingo.bd.R;

public class ResultDialogClass extends Dialog {
    ImageView imageIV;
    LinearLayout enrollLL;
    TextView titleTV;
    boolean ans=false;
    public interface EnrollCallback{
        public void enrollCallback(String id);
    }
    public ResultDialogClass(@NonNull Context context, boolean ans) {
        super(context);
        this.ans=ans;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result_dialog);
        imageIV=findViewById(R.id.imageIV);
        enrollLL=findViewById(R.id.enrollLL);
        titleTV=findViewById(R.id.titleTV);
        if(ans){
            imageIV.setImageResource(R.drawable.correct_answer);
            titleTV.setText("Your answer is Correct");
        }else {
            imageIV.setImageResource(R.drawable.wronganswer);
            titleTV.setText("Your answer is Wrong");
        }
        enrollLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

    }
}
