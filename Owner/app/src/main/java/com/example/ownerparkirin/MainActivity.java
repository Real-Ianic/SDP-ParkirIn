package com.example.ownerparkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        changeFragment(new LocationFragment(), "");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.mylocationsmenu){
                            changeFragment(new LocationFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.transactionsmenu){
                            changeFragment(new TransactionsFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.myprofilemenu){
                            changeFragment(new ProfileFragment(), "");
                        }
                        return true;
                    }
                }
        );

    }



    public void changeFragment(Fragment f, String data) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        f.setArguments(bundle);
        ft.replace(R.id.container, f);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutop,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutmenu){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }




}
