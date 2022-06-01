
package com.codingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.codingo.adapter.CourseAdapter;
import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.adapter.MainSliderAdopter;
import com.codingo.adapter.PicassoImageLoadingService;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.utils.ExpandableHeightGridView;
import com.codingo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.Slider;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ExpandableHeightGridView gridview;
    private Slider banner_slider1;
    private LinearLayout quizLL;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_home, container, false);
        gridview=view.findViewById(R.id.gridview);
        quizLL=view.findViewById(R.id.quizLL);


        Slider.init(new PicassoImageLoadingService(getContext()));
        banner_slider1 = view.findViewById(R.id.banner_slider1);
        List<Integer> banner=new ArrayList<>();

        banner.add(R.drawable.java);
        banner.add(R.drawable.c_plus_plus);
        banner.add(R.drawable.python);



        MainSliderAdopter mainSliderAdopter = new MainSliderAdopter(banner);
        banner_slider1.setClipToOutline(true);
        banner_slider1.setAdapter(mainSliderAdopter);
        banner_slider1.setInterval(3000);
        quizLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),QuizEnrolledCourseActivity.class));
            }
        });
        CourseAdapter courseAdapter = new CourseAdapter(getContext(), new CourseAdapter.CourseIdcallback() {
            @Override
            public void getCourse(String id,Integer image) {
                String mycourses[]=MainActivity.LOGGED_IN_USER.getEnrolled().split(",");
                for(int i=0;i<mycourses.length;i++){
                    if(mycourses[i].equals("course"+id)){
                        Toast.makeText(getContext(),"Already Enrolled",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                CourseEnrollDialogClass cdd = new CourseEnrollDialogClass(getContext(), id, image, new CourseEnrollDialogClass.EnrollCallback() {
                    @Override
                    public void enrollCallback(String id) {
                        new FirebaseManager(getContext()).addEnrolledCourse(MainActivity.LOGGED_IN_USER.getId(), MainActivity.LOGGED_IN_USER.getEnrolled()+"course" + id+",", new FirebaseManager.SuccessListener() {
                            @Override
                            public void getsuccess() {

                                Toast.makeText(getContext(),"Successfully Enrolled",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void getFail() {

                            }
                        });
                    }
                });
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });
        gridview.setExpanded(true);
        gridview.setAdapter(courseAdapter);
/*        Slider.init(new PicassoImageLoadingService(getContext()));
        banner_slider1 = root.findViewById(R.id.banner_slider1);
        List<Integer> banner=new ArrayList<>();

        banner.add(R.drawable.ban1);
        banner.add(R.drawable.ban2);
        banner.add(R.drawable.ban3);
        banner.add(R.drawable.ban4);

        MainSliderAdopter mainSliderAdopter = new MainSliderAdopter(banner);
        banner_slider1.setClipToOutline(true);
        banner_slider1.setAdapter(mainSliderAdopter);
        banner_slider1.setInterval(3000);*/
        return view;

    }
}