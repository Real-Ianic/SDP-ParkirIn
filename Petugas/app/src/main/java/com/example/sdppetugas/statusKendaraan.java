package com.example.sdppetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class statusKendaraan extends AppCompatActivity {
    TextView tvNama;
    Button btnBack,btnCekIn;
    EditText etPlat;
    ArrayList<String> arrInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_kendaraan);
        tvNama = findViewById(R.id.tvNamaPlat);
        btnBack = findViewById(R.id.btnBack);
        btnCekIn = findViewById(R.id.btnCekIn);
        etPlat = findViewById(R.id.etPlatNomer);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(statusKendaraan.this,MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnCekIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPlat.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Berhasil Cek In",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(statusKendaraan.this,MainActivity.class);
                    i.putExtra("nama",tvNama.getText().toString());
                    i.putExtra("plat",etPlat.getText().toString());
                    finish();
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Plat Nomor harus diisi",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Intent penerima = getIntent();
        if(penerima!=null){
            tvNama.setText(penerima.getStringExtra("nama"));
            arrInfo = penerima.getStringArrayListExtra("info");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.optLogout){
            Intent i = new Intent(this,LoginActivity.class);
            finish();
            startActivity(i);
        }
        return true;
    }
}
