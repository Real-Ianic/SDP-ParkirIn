package com.example.parkirin;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btnCurrentLocation;
    DatabaseReference reff;
    lokasi l;
    String email;
    ArrayList<String> listLokasi = new ArrayList<String>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        email = ((MainActivity)getActivity()).email;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public void fetchLastLocation() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        Task<Location> task =fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                    mapFragment.getMapAsync(HomeFragment.this);
                }else{
                    Toast.makeText(getContext(), "Lokasi Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public void retrievedata(){


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        btnCurrentLocation = view.findViewById(R.id.btnLocation);
        reff= FirebaseDatabase.getInstance().getReference("Lokasi");
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLastLocation();
            }
        });
        fetchLastLocation();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            Intent i = new Intent(this.getActivity(),LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        if(item.getItemId()==R.id.menuTrans){
            Intent i = new Intent(this.getActivity(),historyActivity.class);
            i.putExtra("email",((MainActivity)getActivity()).email);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Lokasi Anda").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        googleMap.addMarker(markerOptions);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    lokasi l = data.getValue(lokasi.class);
                    LatLng location = new LatLng(l.getLat(),l.getLng());
                    googleMap.addMarker(new MarkerOptions().position(location).title(l.getNama()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(-7.2618583,112.7484354))
//                .title("Grand City Mall").snippet("Sisa Slot : 20"));
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(-7.291306,112.756635))
//                .title("iSTTS").snippet("Sisa Slot : 2"));
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(-7.2622971,112.7382203))
//                .title("Tunjungan Plaza").snippet("Sisa Slot : 15"));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTitle().equals("Grand City Mall")){
                    Intent i = new Intent(getActivity(),pemesananActivity.class);
                    i.putExtra("tempat","Grand City Mall");
                    i.putExtra("email",email);
                    startActivity(i);
                }
                if(marker.getTitle().equals("iSTTS")){
                    Intent i = new Intent(getActivity(),pemesananActivity.class);
                    i.putExtra("tempat","iSTTS");
                    i.putExtra("email",email);
                    startActivity(i);
                }
                if(marker.getTitle().equals("Tunjungan Plaza")){
                    Intent i = new Intent(getActivity(),pemesananActivity.class);
                    i.putExtra("tempat","Tunjungan Plaza");
                    i.putExtra("email",email);
                    startActivity(i);
                }
                return false;
            }
        });
    }


}
