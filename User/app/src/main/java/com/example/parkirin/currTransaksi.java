package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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



public class currTransaksi extends AppCompatActivity {

    private RecyclerView rvHistory;
    private ArrayList<classtransaksi> listTrans = new ArrayList<classtransaksi>();
    private FirebaseRecyclerOptions<classtransaksi> options;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<classtransaksi, FirebaseViewHolder> adapter;
    private String email;
    private Button btnCancelTransaksi;

    DataSetFire ct1;private DatabaseReference reff,drKendaraan;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_transaksi);
        getSupportActionBar().setTitle("Transaksi Saat Ini");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email=getIntent().getStringExtra("email");
        rvHistory = findViewById(R.id.rvHistoryCurr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        btnCancelTransaksi = findViewById(R.id.btnCancelTransaksi);
        Query query = FirebaseDatabase.getInstance().getReference("currTransaksi").orderByChild("emailcust").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
        query.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<classtransaksi>().setQuery(query,classtransaksi.class).build();
        adapter = new FirebaseRecyclerAdapter<classtransaksi, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull final classtransaksi ct) {
                firebaseViewHolder.tvDurasi.setText("Durasi : "+ ct.getDurasiutktampil());
                firebaseViewHolder.tvJenis.setText("Jenis : "+ct.getJenis());
                firebaseViewHolder.tvKendaraan.setText("Jenis Kendaraan : "+ct.getTipekendaraan());
                firebaseViewHolder.tvPlat.setText("Plat Nomor : "+ct.getPlatnomor());
                firebaseViewHolder.tvTempat.setText("Tempat : "+ct.getTempat());
                firebaseViewHolder.tvTglWaktu.setText("Tanggal & Waktu : "+ct.getWaktutransaksi());
                firebaseViewHolder.tvTotal.setText("Total : "+ct.getTotal()+"");
                if(ct.getStatus().equals("Transaksi Berhasil")){
                    firebaseViewHolder.tvStatus.setTextColor(Color.GREEN);
                }
                if(ct.getStatus().equals("Transaksi Gagal")){
                    firebaseViewHolder.tvStatus.setTextColor(Color.RED);
                }
                firebaseViewHolder.tvStatus.setText(ct.getStatus()+"");
                firebaseViewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drKendaraan = FirebaseDatabase.getInstance().getReference();
                        reff = FirebaseDatabase.getInstance().getReference();
                        ct1=new DataSetFire(ct.getTempat(),ct.getJenis(),ct.getPlatnomor(),ct.getTipekendaraan(),ct.getDurasijam(),ct.getTotal(),ct.getEmailcust(),ct.getWaktutransaksi(),ct.getDurasiutktampil(),"Dibatalkan");
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
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(currTransaksi.this).inflate(R.layout.recyclerhistory2,parent,false));
            }
        };
        rvHistory.setAdapter(adapter);
    }
}
