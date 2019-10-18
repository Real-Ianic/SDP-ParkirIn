package com.example.sdppetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class cekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek);
    }
    public void cekKendaraan(View v){
        Intent i = new Intent(this,statusKendaraan.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.optCek){
            Toast.makeText(getApplicationContext(),"Ini halaman cek kendaraan",Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.optMasuk){
            Intent i = new Intent(this,MainActivity.class);
            finish();
            startActivity(i);
        }
        else if(item.getItemId()==R.id.optStatus){
            Intent i = new Intent(this,statusKendaraan.class);
            finish();
            startActivity(i);
        }
        return true;
    }
}
