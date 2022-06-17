package com.codingo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingo.CourseLessonActivity;
import com.codingo.CourseQuizActivity;
import com.codingo.bd.R;
import com.codingo.utils.Utils;

import org.json.JSONArray;

public class LessonAdopter extends RecyclerView.Adapter<LessonAdopter.ViewHolder> {
    Context context;

    JSONArray lessons;
    String id;

    public LessonAdopter(Context context, JSONArray lessons,String id) {
        this.context = context;
        this.lessons=lessons;
        this.id=id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lesson_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int j) {
        int i=viewHolder.getAdapterPosition();
       String title=  Utils.getStringValue(Utils.getJsonObject(lessons,i),"title");
       String description=  Utils.getStringValue(Utils.getJsonObject(lessons,i),"descriptions");
       String type=  Utils.getStringValue(Utils.getJsonObject(lessons,i),"type");
       if(type.equals("2")){
           viewHolder.premiumIV.setVisibility(View.VISIBLE);
       }else {
           viewHolder.premiumIV.setVisibility(View.GONE);
       }
          viewHolder.titleTV.setText(title);
          viewHolder.lessonTV.setText("0"+(i+1)+":");
          viewHolder.lessonLL.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(type.equals("1")) {
                      Intent i = new Intent(context, CourseLessonActivity.class);
                      i.putExtra("title", title);
                      i.putExtra("id", id);
                      i.putExtra("descriptions", description);
                      i.putExtra("lessons", lessons.toString());
                      i.putExtra("position", ""+j);
                      context.startActivity(i);
                  } if(type.equals("3")) {
                      Intent i = new Intent(context, CourseQuizActivity.class);
                      i.putExtra("title", title);
                      i.putExtra("id", id);
                      context.startActivity(i);
                  }

              }
          });

    }

    @Override
    public int getItemCount() {
        return lessons.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private TextView titleTV;
       private TextView lessonTV;
       private ImageView premiumIV;
       private LinearLayout lessonLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            premiumIV=itemView.findViewById(R.id.premiumIV);
            titleTV=itemView.findViewById(R.id.titleTV);
            lessonTV=itemView.findViewById(R.id.lessonTV);
            lessonLL=itemView.findViewById(R.id.lessonLL);

        }
    }

}