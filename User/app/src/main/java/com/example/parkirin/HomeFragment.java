package com.example.parkirin;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btnCurrentLocation,btnPesan;
    DatabaseReference reff,reff2;
    lokasi l;
    String email,tempat,key;
    ArrayList<String> listLokasi = new ArrayList<String>();
    Double lat,lng;
    ArrayAdapter<String> adapter;
    AutoCompleteTextView editText;
    private SimpleCursorAdapter mAdapter;

    // DATABASE ROOM
    public static MyAppDatabase myAppDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        email = ((MainActivity)getActivity()).email;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLastLocation();
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
        fusedLocationProviderClient.getLastLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLastLocation();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        btnCurrentLocation = view.findViewById(R.id.btnLocation);
        btnPesan = view.findViewById(R.id.btnPesan);
        editText = view.findViewById(R.id.actv);

        myAppDatabase = Room.databaseBuilder(getContext(),MyAppDatabase.class,"searchhistory").allowMainThreadQueries().build();

        reff= FirebaseDatabase.getInstance().getReference("Lokasi");
        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLastLocation();
            }
        });

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    lokasi l = data.getValue(lokasi.class);
                    listLokasi.add(l.getNama());
                    adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listLokasi);
                    editText.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(),LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();
        }
        if(item.getItemId()==R.id.menuTrans){
            Intent i = new Intent(this.getActivity(),currTransaksi.class);
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

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                Query query1 = rootRef.child("Lokasi").orderByChild("nama").equalTo(editText.getText().toString());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            key = ds.getKey();
                            reff2 = FirebaseDatabase.getInstance().getReference().child("Lokasi").child(key);
                            reff2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    NumberFormat f = NumberFormat.getInstance();
                                    try {
                                        lat = f.parse(dataSnapshot.child("lat").getValue().toString()).doubleValue();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        lng = f.parse(dataSnapshot.child("lng").getValue().toString()).doubleValue();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    LatLng lokasisearch = new LatLng(lat,lng);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasisearch,18));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                query1.addListenerForSingleValueEvent(valueEventListener);

                //Adding History To Room
                String temp = editText.getText().toString();
                SearchHistory halo = new SearchHistory(temp);

                try {
                    myAppDatabase.myDao().addSearchhistory(halo);
                }
                catch(Exception e) {
                    System.out.println(e.toString());
                    System.out.println("GAGAL MASUK ROOM");
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<SearchHistory> listHistory = myAppDatabase.myDao().getHistory();
                ArrayList<String> listHis = new ArrayList<String>();

                for(int i=0;i<listHistory.size();i++)
                {
                    listHis.add(listHistory.get(i).judul);
                }

                adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listHis);
                editText.setAdapter(adapter);
                editText.showDropDown();
            }
        });


        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                Toast.makeText(getContext(), "a", Toast.LENGTH_SHORT).show();
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btnPesan.setVisibility(View.INVISIBLE);
                btnPesan.setOnClickListener(null);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                editText.setText(null);
            }
        });

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
//                searchView.setSuggestionsAdapter();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                btnPesan.setVisibility(View.VISIBLE);
                btnPesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!marker.getTitle().equals("Lokasi Anda")){
                            Intent i = new Intent(getContext(),tempatdetail.class);
                            i.putExtra("tempat",marker.getTitle());
                            i.putExtra("email",email);
                            i.putExtra("currlat",currentLocation.getLatitude()+"");
                            i.putExtra("currlng",currentLocation.getLongitude()+"");
                            startActivity(i);
                        }else{
                            Toast.makeText(getContext(), "Lokasi Anda", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return false;
            }
        });
    }
}
