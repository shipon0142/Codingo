
package com.codingo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.codingo.adapter.CourseAdapter;
import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.adapter.MainSliderAdopter;
import com.codingo.adapter.PicassoImageLoadingService;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.model.Course;
import com.codingo.utils.ExpandableHeightGridView;
import com.codingo.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private LinearLayout myCourseLL;
    private LinearLayout searchLL;
    private LinearLayout cancelLL;
    private LinearLayout AllCourseLL;
    private LinearLayout mainToolbarLL;
    private ImageView searchIV;
    private EditText searchET;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        gridview = view.findViewById(R.id.gridview);
        quizLL = view.findViewById(R.id.quizLL);
        cancelLL = view.findViewById(R.id.cancelLL);
        myCourseLL = view.findViewById(R.id.myCourseLL);
        searchLL = view.findViewById(R.id.searchLL);
        mainToolbarLL = view.findViewById(R.id.mainToolbarLL);
        searchIV = view.findViewById(R.id.searchIV);
        searchET = view.findViewById(R.id.searchET);
        AllCourseLL = view.findViewById(R.id.AllCourseLL);


        Slider.init(new PicassoImageLoadingService(getContext()));
        banner_slider1 = view.findViewById(R.id.banner_slider1);
        List<Integer> banner = new ArrayList<>();

        banner.add(R.drawable.java);
        banner.add(R.drawable.php);
        banner.add(R.drawable.python);


        MainSliderAdopter mainSliderAdopter = new MainSliderAdopter(banner);
        banner_slider1.setClipToOutline(true);
        banner_slider1.setAdapter(mainSliderAdopter);
        banner_slider1.setInterval(3000);
        quizLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QuizEnrolledCourseActivity.class));
            }
        });
        myCourseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), YourEnrolledCourseActivity.class));
            }
        });
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainToolbarLL.setVisibility(View.GONE);
                searchET.requestFocus();
                searchET.requestLayout();
            }
        });
        searchET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) mainToolbarLL.setVisibility(View.VISIBLE);
            }
        });
        AllCourseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), YourSearchCourseActivity.class);
                startActivity(intent);
            }
        });
        searchET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_ENTER:
                            if (searchET.getText().toString().equals("")) {
                                searchET.clearFocus();
                                hideKeyboard(Objects.requireNonNull(getActivity()));
                            } else {
                                String tex = searchET.getText().toString();
                                searchET.setText("");
                                searchET.clearFocus();
                                hideKeyboard(Objects.requireNonNull(getActivity()));
                                Intent intent = new Intent(getContext(), YourSearchCourseActivity.class);
                                intent.putExtra("text", tex);
                                startActivity(intent);
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        cancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainToolbarLL.setVisibility(View.VISIBLE);
                hideKeyboard(Objects.requireNonNull(getActivity()));
            }
        });

        CourseAdapter courseAdapter = new CourseAdapter(getContext(), new CourseAdapter.CourseIdcallback() {
            @Override
            public void getCourse(String id, Integer image) {
                String mycourses[] = MainActivity.LOGGED_IN_USER.getEnrolled().split(",");
                for (int i = 0; i < mycourses.length; i++) {
                    if (mycourses[i].equals("course" + id)) {
                      //  Toast.makeText(getContext(), "Already Enrolled", Toast.LENGTH_SHORT).show();


                            Intent ii = new Intent(getContext(), CourseDescriptionActivity.class);
                            ii.putExtra("title", getCourse2(id).getName());
                            ii.putExtra("id", getCourse2(id).getId());
                            startActivity(ii);

                        return;
                    }
                }
                if(Utils.getStringValue(Utils.getJsonObject(MainActivity.PUBLIC_JSON_OBJECT, "id"+id), "enable").equals("0")){
                    Toast.makeText(getContext(), "This course will be enable soon", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < MainActivity.courses.size(); i++) {
                    if (MainActivity.courses.get(i).getId().equals(id)) {
                        Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                        intent.putExtra("title", MainActivity.courses.get(i).getName());
                        intent.putExtra("id", id);
                        intent.putExtra("description", Utils.getStringValue(Utils.getJsonObject(MainActivity.PUBLIC_JSON_OBJECT, "id"+id), "content"));
                        startActivity(intent);
                        break;
                    }

                }

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

    private Course getCourse2(String id) {
        for (int i = 0; i < MainActivity.courses.size(); i++) {
            if (MainActivity.courses.get(i).getId().equals(id)) {
                return MainActivity.courses.get(i);
            }
        }
        return new Course();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}