package com.android.spaciocrm.home.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.adapter.ReferralFragmentPageAdapter;
import com.android.spaciocrm.util.dialogs.NewReferralDialogFragment;

public class FragReferrals extends Fragment {

    private static final String ARG_TITLE = "refTitle";
    private String mTitle;

    public FragReferrals() {
        // Required empty public constructor
    }

    public static FragReferrals newInstance(String title) {
        FragReferrals fragment = new FragReferrals();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_referrals, container, false);

        ViewPager viewpager = view.findViewById(R.id.referral_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tabReferrals);

        FloatingActionButton btnNewReferral = view.findViewById(R.id.btnFloatingAddReferrals);

        FragmentManager manager = getChildFragmentManager();

        ReferralFragmentPageAdapter adapter = new ReferralFragmentPageAdapter(manager);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);

        btnNewReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewReferralDialogFragment newReferralDialogFragment = new NewReferralDialogFragment();
                newReferralDialogFragment.show(getActivity().getSupportFragmentManager(),"NewReferralFragment");
            }
        });

        return view;
    }

}