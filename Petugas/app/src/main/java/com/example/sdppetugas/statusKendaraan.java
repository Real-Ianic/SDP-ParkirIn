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
        final String status = getIntent().getStringExtra("status");
        String durasi = getIntent().getStringExtra("durasi");
        int total = getIntent().getIntExtra("total",0);
        if(status.equals("Menunggu Konfirmasi Petugas")){
            btnCekOut.setEnabled(false);
            btnCekIn.setEnabled(true);
        }else if(status.equals("Diterima")){
            btnCekIn.setEnabled(false);
            btnCekOut.setEnabled(true);
            btnBack.setEnabled(false);
        }
        else if(status.equals("Ditolak")){
            btnCekIn.setEnabled(false);
            btnCekOut.setEnabled(false);
            btnBack.setEnabled(false);
        }
        else if(status.equals("Selesai")){
            btnBack.setEnabled(false);
            btnCekOut.setEnabled(false);
            btnCekIn.setEnabled(false);
        }
        tvPlat.setText(plat);
        tvDurasi.setText(durasi);
        tvTotal.setText("Rp. " + total);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TolakKendaraan(plat);
                Intent i = new Intent(statusKendaraan.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnCekIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CekInKendaraan(plat);
                Intent i = new Intent(statusKendaraan.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
        btnCekOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CekOutKendaraan(plat);
                Intent i = new Intent(statusKendaraan.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
    String key;
    DatabaseReference reff,reff2;
    DataSetFire dsClass;
    public void CekOutKendaraan(String plat){
        DatabaseReference drKendaraan = FirebaseDatabase.getInstance().getReference();
        Query query = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(plat);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("currTransaksi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reff.child("status").setValue("Selesai");
                            Toast.makeText(getApplicationContext(),"Selesai",Toast.LENGTH_SHORT).show();
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
        query.addValueEventListener(valueEventListener);
    }
    public void CekInKendaraan(String plat){
        DatabaseReference drKendaraan = FirebaseDatabase.getInstance().getReference();
        Query query = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(plat);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("currTransaksi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reff.child("status").setValue("Diterima");
                            Toast.makeText(getApplicationContext(),"Diterima",Toast.LENGTH_SHORT).show();
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
        query.addValueEventListener(valueEventListener);
    }
    public void TolakKendaraan(String plat){
        DatabaseReference drKendaraan = FirebaseDatabase.getInstance().getReference();
        Query query = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(plat);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("currTransaksi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reff.child("status").setValue("Ditolak");
                            final DatabaseReference drCustomer = FirebaseDatabase.getInstance().getReference().child("Member").child(reff.child("emailcust")+"");
                            drCustomer.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    drCustomer.child("saldo").setValue(Integer.parseInt(reff.child("total").toString())+dataSnapshot.child("saldo").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
        query.addValueEventListener(valueEventListener);
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
