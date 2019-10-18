package com.example.parkirin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(-7.2613561,112.6839624))
                        .zoom(11)
                        .bearing(0)
                        .tilt(0)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 100, null);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-7.2618583,112.7484354))
                        .title("Grand City Mall").snippet("Sisa Slot : 20"));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-7.291306,112.756635))
                        .title("iSTTS").snippet("Sisa Slot : 2"));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-7.2622971,112.7382203))
                        .title("Tunjungan Plaza").snippet("Sisa Slot : 15"));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getTitle().equals("Grand City Mall")){
                            Intent i = new Intent(getActivity(),pemesananActivity.class);
                            i.putExtra("tempat","Grand City Mall");
                            startActivity(i);
                        }
                        if(marker.getTitle().equals("iSTTS")){
                            Intent i = new Intent(getActivity(),pemesananActivity.class);
                            i.putExtra("tempat","iSTTS");
                            startActivity(i);
                        }
                        if(marker.getTitle().equals("Tunjungan Plaza")){
                            Intent i = new Intent(getActivity(),pemesananActivity.class);
                            i.putExtra("tempat","Tunjungan Plaza");
                            startActivity(i);
                        }
                        return false;
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
