
package com.codingo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.codingo.adapter.MainSliderAdopter;
import com.codingo.adapter.PicassoImageLoadingService;
import com.codingo.bd.R;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.Slider;

public class SubscriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Slider banner_slider1;
    BillingClient billingClient;
    int selectedPosition = -1;

    public SubscriptionFragment() {
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
    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
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

    LinearLayout buySubscriptionLL;
    LinearLayout pack1LL;
    LinearLayout pack2LL;
    LinearLayout pack3LL;
    LinearLayout pack4LL;
    ArrayList<LinearLayout> packages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_subscription, container, false);
        billingClient = BillingClient.newBuilder(getActivity())
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
                                                   @Nullable List<Purchase> list) {

                    }
                })
                .build();
        buySubscriptionLL = view.findViewById(R.id.buySubscriptionLL);
        pack1LL = view.findViewById(R.id.pack1LL);
        pack2LL = view.findViewById(R.id.pack2LL);
        pack3LL = view.findViewById(R.id.pack3LL);
        pack4LL = view.findViewById(R.id.pack4LL);
        packages = new ArrayList<>();
        packages.add(pack1LL);
        packages.add(pack2LL);
        packages.add(pack3LL);
        packages.add(pack4LL);

        Slider.init(new PicassoImageLoadingService(getContext()));
        banner_slider1 = view.findViewById(R.id.banner_slider1);
        List<Integer> banner = new ArrayList<>();

        banner.add(R.drawable.sub1);
        banner.add(R.drawable.sub2);
        banner.add(R.drawable.sub3);

        pack1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(0);
            }
        });
        pack2LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(1);
            }
        });
        pack3LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(2);
            }
        });
        pack4LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(3);
            }
        });

        MainSliderAdopter mainSliderAdopter = new MainSliderAdopter(banner);
        banner_slider1.setClipToOutline(true);
        banner_slider1.setAdapter(mainSliderAdopter);
        banner_slider1.setInterval(3000);
        billingClient = BillingClient.newBuilder(getContext())
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
                                                   @Nullable List<Purchase> list) {
                    }
                }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

            }
        });
        List<String> productIds = new ArrayList<>();
        productIds.add("monthlypackcodingo1");
        productIds.add("monthlypackcodingo2");
        productIds.add("monthlypackcodingo3");
        productIds.add("monthlypackcodingo4");
        SkuDetailsParams getProductDetailsQuery =
                SkuDetailsParams
                        .newBuilder()
                        .setSkusList(productIds)
                        .setType(BillingClient.SkuType.INAPP)
                        .build();

       /* SkuDetails itemInfo = list.get(0);
        itemNameTextView.setText(itemInfo.getTitle());
        itemPriceButton.setText(itemInfo.getPrice());*/
        buySubscriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPosition == -1) {
                    Toast.makeText(getContext(), "Please select a package", Toast.LENGTH_SHORT).show();
                } else {

                    billingClient.querySkuDetailsAsync(
                            getProductDetailsQuery,
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(
                                        @NonNull BillingResult billingResult,
                                        @Nullable List<SkuDetails> list) {

                                    billingClient.launchBillingFlow(
                                            getActivity(),
                                            BillingFlowParams
                                                    .newBuilder()
                                                    .setSkuDetails(list.get(selectedPosition))
                                                    .build());

                                }
                            });

                }
            }
        });

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

    private void select(int position) {
        selectedPosition = position;
        for (int i = 0; i < packages.size(); i++) {
            if (i == position)
                packages.get(i).setBackgroundResource(R.drawable.subscription_price_background_selected);
            else packages.get(i).setBackgroundResource(R.drawable.subscription_price_background);
        }
    }
}