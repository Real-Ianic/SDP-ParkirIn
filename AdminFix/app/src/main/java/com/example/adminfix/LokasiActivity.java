package com.example.adminfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LokasiActivity extends AppCompatActivity {

    Button addLokasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        addLokasi = findViewById(R.id.btnAddLokasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LokasiActivity.this,AddLokasiActivity.class);
                startActivity(i);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.menuHome){
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);        }
        return true;
    }
}
