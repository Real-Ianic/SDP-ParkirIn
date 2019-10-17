package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdminHome extends AppCompatActivity {
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);



    }

    public void openManageUsers(View v){
        Intent i = new Intent(this, AdminManageUser.class);
        startActivity(i);
    }

    public void openManageWorkers(View v){
        Intent i = new Intent(this, AdminManageWorker.class);
        startActivity(i);
    }

    public void openManagePlaces(View v){
        Intent i = new Intent(this, AdminManagePlaces.class);
        startActivity(i);
    }

    public void openReports(View v){
        Intent i = new Intent(this, AdminReports.class);
        startActivity(i);
    }



}
