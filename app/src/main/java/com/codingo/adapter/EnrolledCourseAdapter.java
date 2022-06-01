package com.codingo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.codingo.CourseDescriptionActivity;
import com.codingo.CourseQuizActivity;
import com.codingo.MainActivity;
import com.codingo.bd.R;
import com.codingo.model.Course;

import java.util.Arrays;
import java.util.List;

public class EnrolledCourseAdapter extends BaseAdapter {
    private Context mContext;


    //List<String> TITLE = Arrays.asList("Java Programming", "C++ Programming", "Python Programming", "C# Programming", "Web development", "Version Control");

    String courseid[];
    boolean forQuiz = false;

    // Constructor
    public EnrolledCourseAdapter(Context c) {
        mContext = c;

        courseid = MainActivity.LOGGED_IN_USER.getEnrolled().split(",");

    }public EnrolledCourseAdapter(Context c,boolean forQuiz) {
        mContext = c;
        courseid = MainActivity.LOGGED_IN_USER.getEnrolled().split(",");
        this.forQuiz=true;

    }

    public int getCount() {
        int size = 0;
        if (courseid.length == 1 && courseid[0].equals("")) size = 0;
        else {
            size = courseid.length;
        }
        return size;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        String id = courseid[position].replace("course", "");
        TextView titleTV;
        ImageView imageIV;

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.enrolled_course, null);
        }
        titleTV = convertView.findViewById(R.id.titleTV);
        imageIV = convertView.findViewById(R.id.imageIV);
        titleTV.setText(getCourse(id).getName());
        imageIV.setImageResource(getCourse(id).getImage());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forQuiz){
                    Intent i = new Intent(mContext, CourseQuizActivity.class);
                    i.putExtra("title", getCourse(id).getName());
                    i.putExtra("id", getCourse(id).getId());
                    mContext.startActivity(i);
                }else {
                    Intent i = new Intent(mContext, CourseDescriptionActivity.class);
                    i.putExtra("title", getCourse(id).getName());
                    i.putExtra("id", getCourse(id).getId());
                    mContext.startActivity(i);
                }

            }
        });


        return convertView;
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
