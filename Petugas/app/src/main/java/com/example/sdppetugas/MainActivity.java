package com.example.sdppetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private FirebaseRecyclerOptions<DataSetFire> options;
    private DatabaseReference databaseReference,reff,drCustomer;
    DatabaseReference drKendaraan;
    Query reffLokasi,reffLokasiNama,query;
    private FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder> adapter;
    private String key,idLokasi,namaLokasi,idCurr;
    int saldo;
    DataSetFire ct1;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void deletetrans(String id){

    }
    public void getReff(String key){
        reff = FirebaseDatabase.getInstance().getReference().child("currTransaksi").child(key);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvHistory = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        reffLokasi = FirebaseDatabase.getInstance().getReference("Petugas").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reffLokasi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idLokasi = dataSnapshot.child("idLokasi").getValue().toString();
                Toast.makeText(MainActivity.this, idLokasi+"", Toast.LENGTH_SHORT).show();
                reffLokasiNama = FirebaseDatabase.getInstance().getReference("Lokasi").child(idLokasi);
                reffLokasiNama.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        namaLokasi = dataSnapshot.child("nama").getValue().toString();
                        Toast.makeText(MainActivity.this, namaLokasi, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Query idTrans = FirebaseDatabase.getInstance().getReference("currTransaksi").orderByChild("tempat").equalTo(namaLokasi);
//        idTrans.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                idCurr=dataSnapshot.getKey();
//                Toast.makeText(MainActivity.this, idCurr, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        query = FirebaseDatabase.getInstance().getReference("currTransaksi").orderByChild("tempat");
        query.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(query,DataSetFire.class).build();
        adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, int i, @NonNull final DataSetFire ct) {

                drCustomer = FirebaseDatabase.getInstance().getReference().child("Member").child(ct.getEmailcust());
                drCustomer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        saldo = Integer.parseInt(dataSnapshot.child("saldo").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseViewHolder.tvDurasi.setText("Durasi : "+ ct.getDurasiutktampil());
                firebaseViewHolder.tvJenis.setText("Jenis : "+ct.getJenis());
                firebaseViewHolder.tvKendaraan.setText("Jenis Kendaraan : "+ct.getTipekendaraan());
                firebaseViewHolder.tvPlat.setText("Plat Nomor : "+ct.getPlatnomor());
                firebaseViewHolder.tvTempat.setText("Tempat : "+ct.getTempat());
                firebaseViewHolder.tvTglWaktu.setText("Tanggal & Waktu : "+ct.getWaktutransaksi());
                firebaseViewHolder.tvTotal.setText("Total : "+ct.getTotal()+"");
                if(ct.getStatus().equals("Menunggu Konfirmasi Petugas")){
                    firebaseViewHolder.btnCheckout.setEnabled(false);
                }
                else if(ct.getStatus().equals("Diterima")){
                    firebaseViewHolder.btnCheckin.setEnabled(false);
                    firebaseViewHolder.btnTolak.setEnabled(false);
                }
                firebaseViewHolder.btnCheckin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drKendaraan = FirebaseDatabase.getInstance().getReference();
                        Query query1 = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(ct.getPlatnomor());
                        ValueEventListener valueEventListener1 = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                                    key = ds.getKey();
                                    reff = FirebaseDatabase.getInstance().getReference().child("currTransaksi").child(key);
                                    reff.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            reff.child("status").setValue("Diterima");
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
                        query1.addValueEventListener(valueEventListener1);
                    }
                });
                firebaseViewHolder.btnCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drKendaraan = FirebaseDatabase.getInstance().getReference();
                        reff = FirebaseDatabase.getInstance().getReference();
                        ct1=new DataSetFire(ct.getTempat(),ct.getJenis(),ct.getPlatnomor(),ct.getTipekendaraan(),ct.getDurasijam(),ct.getTotal(),ct.getEmailcust(),ct.getWaktutransaksi(),ct.getDurasiutktampil(),"Selesai");
                        reff.child("Transaksi").push().setValue(ct1);
                        Query query2 = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(ct.getPlatnomor());
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot d : dataSnapshot.getChildren()){
                                    d.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                firebaseViewHolder.btnTolak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drKendaraan = FirebaseDatabase.getInstance().getReference();
                        drCustomer.child("saldo").setValue(saldo+ct.getTotal());
                        reff = FirebaseDatabase.getInstance().getReference();
                        ct1=new DataSetFire(ct.getTempat(),ct.getJenis(),ct.getPlatnomor(),ct.getTipekendaraan(),ct.getDurasijam(),ct.getTotal(),ct.getEmailcust(),ct.getWaktutransaksi(),ct.getDurasiutktampil(),"Ditolak");
                        reff.child("Transaksi").push().setValue(ct1);
                        Query query2 = drKendaraan.child("currTransaksi").orderByChild("platnomor").equalTo(ct.getPlatnomor());
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot d : dataSnapshot.getChildren()){
                                    d.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    //delete

                });
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.row,parent,false));
            }
        };
        rvHistory.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.optLogout){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
