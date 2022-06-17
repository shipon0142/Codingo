
package com.codingo;

import static android.content.Context.MODE_PRIVATE;
import static com.codingo.MainActivity.LOGGED_IN_USER;
import static com.codingo.MainActivity.sharedPreferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codingo.adapter.EnrolledCourseAdapter;
import com.codingo.bd.BuildConfig;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.utils.ExpandableHeightGridView;
import com.codingo.utils.Utils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private CircleImageView profileIV;
    private ExpandableHeightGridView gridview;
    private LinearLayout logoutLL;
    private LinearLayout privacyPolicyLL;
    private TextView emailTV;
    private TextView nameTV;
    private LinearLayout backToHomeLL;
    private LinearLayout aboutUsLL;
    private LinearLayout contactUsLL;
    private LinearLayout myEnrolledLL;
    private LinearLayout myRateUsLL;
    private LinearLayout shareAppLL;

    public MeFragment() {
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
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
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
        view = inflater.inflate(R.layout.fragment_me, container, false);
        gridview = view.findViewById(R.id.gridview);
        profileIV = view.findViewById(R.id.profileIV);
        emailTV = view.findViewById(R.id.emailTV);
        nameTV = view.findViewById(R.id.nameTV);
        logoutLL = view.findViewById(R.id.logoutLL);
        backToHomeLL = view.findViewById(R.id.backToHomeLL);
        privacyPolicyLL = view.findViewById(R.id.privacyPolicyLL);
        aboutUsLL = view.findViewById(R.id.aboutUsLL);
        contactUsLL = view.findViewById(R.id.contactUsLL);
        myEnrolledLL = view.findViewById(R.id.myEnrolledLL);
        myRateUsLL = view.findViewById(R.id.myRateUsLL);
        shareAppLL = view.findViewById(R.id.shareAppLL);
        nameTV.setText(LOGGED_IN_USER.getName());
        emailTV.setText(LOGGED_IN_USER.getEmail());
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        MainActivity.myEdit = sharedPreferences.edit();
        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* LogoutConfirmation cdd = new LogoutConfirmation(getContext(), new LogoutConfirmation.EnrollCallback() {
                    @Override
                    public void enrollCallback(String id) {

                    }
                });
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog))
                        .setCancelable(true)

                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.myEdit.clear();
                                MainActivity.myEdit.apply();
                                Intent openstartingpoint = new Intent(getContext(), com.codingo.LoginActivity.class);
                                openstartingpoint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(openstartingpoint);
                                getActivity().finish();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Toast.makeToast(mContext, "No", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();


            }
        });
        backToHomeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.backToHomeListener.backToHomeClick();
            }
        });
        privacyPolicyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WebViewContentActivity.class);
                i.putExtra("title", "Privacy Policy");
                i.putExtra("content", Utils.getStringValue(MainActivity.PUBLIC_JSON_OBJECT, "privacy_policy"));
                startActivity(i);
            }
        });
        myEnrolledLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), YourEnrolledCourseActivity.class));
            }
        });
        shareAppLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        aboutUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WebViewContentActivity.class);
                i.putExtra("title", "About Us");
                i.putExtra("content", Utils.getStringValue(MainActivity.PUBLIC_JSON_OBJECT, "about_us"));
                startActivity(i);
            }
        });
        contactUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WebViewContentActivity.class);
                i.putExtra("title", "Contact Us");
                i.putExtra("content", Utils.getStringValue(MainActivity.PUBLIC_JSON_OBJECT, "contact_us"));
                startActivity(i);
            }
        });
        myRateUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateUsDialogClass cdd = new RateUsDialogClass(getContext());
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });
        Picasso.get().load(LOGGED_IN_USER.getPhoto()).into(profileIV);
        EnrolledCourseAdapter courseAdapter = new EnrolledCourseAdapter(getContext());
        gridview.setExpanded(true);
        gridview.setAdapter(courseAdapter);
        emailTV.setText(LOGGED_IN_USER.getEmail());
        return view;

    }

}