package com.example.guidedtrainingapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.guidedtrainingapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double latitude=-33.924167, longitude=150.882190; // default location
    private float zoomLevel = 5.0f;
    private String title = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        mapFragment.getMapAsync(this);
        return view ;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in location and move the camera
        LatLng loc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(loc).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
    }

    public void setRecordLocationOnMap(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;

        onMapReady(mMap);
    }
}
