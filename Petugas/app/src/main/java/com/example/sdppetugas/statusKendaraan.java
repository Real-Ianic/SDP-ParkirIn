package com.example.sdppetugas;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class statusKendaraan extends AppCompatActivity {
    TextView tvNama,tvPlat,tvDurasi,tvTotal;
    Button btnBack,btnCekIn,btnCekOut;
    EditText etPlat;
    ArrayList<String> arrInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_kendaraan);
        tvNama = findViewById(R.id.tvNamaPlat);
        btnBack = findViewById(R.id.btnBack);
        btnCekIn = findViewById(R.id.btnCekIn);
        btnCekOut = findViewById(R.id.btnCekOut);
        tvPlat = findViewById(R.id.tvPlatNomor);
        tvDurasi = findViewById(R.id.tvDurasiWaktu);
        tvTotal = findViewById(R.id.tvTotal);
        final String plat = getIntent().getStringExtra("plat");
        String status = getIntent().getStringExtra("status");
        String durasi = getIntent().getStringExtra("durasi");
        int total = getIntent().getIntExtra("total",0);
        if(status.equals("Menunggu Konfirmasi Petugas")){
            btnCekOut.setEnabled(false);
            btnCekIn.setEnabled(true);
        }else if(status.equals("Diterima")){
            btnCekIn.setEnabled(false);
            btnCekOut.setEnabled(true);
        }
        tvPlat.setText(plat);
        tvDurasi.setText(durasi);
        tvTotal.setText("Rp. " + total);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(statusKendaraan.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnCekIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TolakKendaraan(plat);
                Toast.makeText(getApplicationContext(),"Berhasil Cek In",Toast.LENGTH_LONG).show();
                Intent i = new Intent(statusKendaraan.this, MainActivity.class);
                finish();
            }
        });
    }
    String key;
    DatabaseReference reff;
    DataSetFire dsClass;
    public void TolakKendaraan(String plat){
        DatabaseReference drKendaraan = FirebaseDatabase.getInstance().getReference();
        Query query = drKendaraan.child("Transaksi");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dsClass = new DataSetFire(tvPlat.getText().toString(),tvDurasi.getText().toString(),"Ditolak");
                            reff.setValue(dsClass);
                            Toast.makeText(getApplicationContext(),"Ditolak",Toast.LENGTH_SHORT).show();
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.optLogout){
            Intent i = new Intent(this, LoginActivity.class);
            finish();
            startActivity(i);
        }
        return true;
    }
}
