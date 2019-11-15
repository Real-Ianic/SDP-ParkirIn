package com.example.adminfix;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNav);
        changeFragment(new LocationFragment(), "");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menuLocation){
                            changeFragment(new LocationFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuUser){
                            changeFragment(new UserFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuOperator){
                            changeFragment(new OperatorFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuOwner){
                            changeFragment(new OwnerFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuTransaction){
                            changeFragment(new TransactionFragment(), "");
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            finish();
        }
        return true;
    }
}
