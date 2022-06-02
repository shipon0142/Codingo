package com.codingo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingo.MainActivity;
import com.codingo.bd.R;
import com.codingo.model.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends BaseAdapter {
    private Context mContext;
    private boolean forSearch;
    private String text;


    //List<String> TITLE = Arrays.asList("Java Programming", "C++ Programming", "Python Programming", "C# Programming", "Web development", "Version Control");
    ArrayList<Course> courses = new ArrayList<>();

    // Constructor
    public interface CourseIdcallback {
        public void getCourse(String id, Integer image);
    }

    CourseIdcallback courseIdcallback;

    public CourseAdapter(Context c, CourseIdcallback courseIdcallback) {
        mContext = c;
        courses = new ArrayList<>();
        courses = MainActivity.courses;
        this.courseIdcallback = courseIdcallback;

    }

    public CourseAdapter(Context c, boolean forSearch, String text, CourseIdcallback courseIdcallback) {
        mContext = c;
        this.forSearch = forSearch;
        this.text = text;
        courses = new ArrayList<>();
        if(!text.equals("")) {
            for (int i = 0; i < MainActivity.courses.size(); i++) {
                if (MainActivity.courses.get(i).getName().toLowerCase().contains(text.toLowerCase())) {
                    courses.add(MainActivity.courses.get(i));
                }

            }
        }else {
            courses = MainActivity.courses;
        }


        this.courseIdcallback = courseIdcallback;

    }

    public int getCount() {
        return courses.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView titleTV;
        ImageView imageIV;

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.course, null);
        }
        titleTV = convertView.findViewById(R.id.titleTV);
        imageIV = convertView.findViewById(R.id.imageIV);
        titleTV.setText(courses.get(position).getName());
        imageIV.setImageResource(courses.get(position).getImage());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseIdcallback.getCourse(courses.get(position).getId(), courses.get(position).getImage());
            }
        });


        return convertView;
    }


}
