package com.example.ownerparkirin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    BottomNavigationView bottomNavLocation;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavLocation = getView().findViewById(R.id.bottomNavLocation);
        changeLocationFragment(new YourLocationsFragment(), "");
        bottomNavLocation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.addlocationmenu){
                            changeLocationFragment(new AddLocationFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.locationsmenu){
                            changeLocationFragment(new YourLocationsFragment(), "");
                        }
                        return true;
                    }
                }
        );
    }


    public void changeLocationFragment(Fragment f, String data) {
        FragmentManager fz = getFragmentManager();
        FragmentTransaction ft = fz.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        f.setArguments(bundle);
        ft.replace(R.id.containerLocation, f);
        ft.commit();
    }


}
