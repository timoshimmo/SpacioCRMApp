package com.android.spaciocrm.home.fragments.contacts;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.spaciocrm.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private static final String ARG_ADDRESS = "param_address";
    private static final String ARG_TITLE = "param_title";

    private String mParamAddy;
    private String mParamTitle;

    public FragmentMap() {
        // Required empty public constructor
    }

    public static FragmentMap newInstance(String paramAddress, String paramTitle) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, paramAddress);
        args.putString(ARG_TITLE, paramTitle);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamAddy = getArguments().getString(ARG_ADDRESS);
            mParamTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.MapView);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;

        try {

            address = coder.getFromLocationName(mParamAddy,5);

            if (address == null) {
                return;
            }

            Address location=address.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(mParamTitle));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        catch (IOException e) {

            e.printStackTrace();
        }

    }
}
