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
import java.util.ArrayList;
import java.util.List;

public class FragmentMultipleMarkersMap extends Fragment implements OnMapReadyCallback {

    private static final String ARG_ADDRESSES = "param_addresses";
    private static final String ARG_TITLES = "param_titles";

    private ArrayList<String> mParam1;
    private ArrayList<String> mParam2;


    public FragmentMultipleMarkersMap() {
        // Required empty public constructor
    }

    public static FragmentMultipleMarkersMap newInstance(ArrayList<String> param1, ArrayList<String> param2) {
        FragmentMultipleMarkersMap fragment = new FragmentMultipleMarkersMap();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ADDRESSES, param1);
        args.putStringArrayList(ARG_TITLES, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_ADDRESSES);
            mParam2 = getArguments().getStringArrayList(ARG_TITLES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_multiple_markers_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapMarkers);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for(int i=0; i < mParam1.size(); i++) {

            Geocoder coder = new Geocoder(getActivity());
            List<Address> address;

            String addy = mParam1.get(i);
            String title = mParam2.get(i);

            try {

                address = coder.getFromLocationName(addy, 5);

                if (address == null) {
                    return;
                }

                Address location=address.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
