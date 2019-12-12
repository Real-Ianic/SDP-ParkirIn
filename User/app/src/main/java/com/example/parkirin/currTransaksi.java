package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
        Query query = FirebaseDatabase.getInstance().getReference("currTransaksi").orderByChild("emailcust").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
        query.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<classtransaksi>().setQuery(query,classtransaksi.class).build();
        adapter = new FirebaseRecyclerAdapter<classtransaksi, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull classtransaksi ct) {
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
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(currTransaksi.this).inflate(R.layout.recyclerhistory,parent,false));
            }
        };
        rvHistory.setAdapter(adapter);
    }
}
