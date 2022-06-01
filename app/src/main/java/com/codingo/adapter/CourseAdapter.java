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

import java.util.Arrays;
import java.util.List;

public class CourseAdapter extends BaseAdapter {
    private Context mContext;


    //List<String> TITLE = Arrays.asList("Java Programming", "C++ Programming", "Python Programming", "C# Programming", "Web development", "Version Control");


    // Constructor
    public interface CourseIdcallback {
        public void getCourse(String id,Integer image);
    }

    CourseIdcallback courseIdcallback;

    public CourseAdapter(Context c, CourseIdcallback courseIdcallback) {
        mContext = c;
        this.courseIdcallback = courseIdcallback;

    }

    public int getCount() {
        return MainActivity.courses.size();
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
        titleTV.setText(MainActivity.courses.get(position).getName());
        imageIV.setImageResource(MainActivity.courses.get(position).getImage());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseIdcallback.getCourse(MainActivity.courses.get(position).getId(),MainActivity.courses.get(position).getImage());
            }
        });


        return convertView;
    }


}
