package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailTransaksi extends AppCompatActivity {

    TextView tvTempat,tvPlat,tvJenis,tvKendaraan,tvDurasi,tvJumlah,tvSaldo;
    String jenis,tempat,platnomor,durasi,kendaraan,waktu,email;
    private DatabaseReference reff;
    private DatabaseReference reffLokasi;
    classtransaksi ct;
    Date currentTime = Calendar.getInstance().getTime();
    DatabaseReference hargaRef = FirebaseDatabase.getInstance().getReference("Harga");
    DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss");
    String strDate = dateFormat.format(currentTime);
    DatabaseReference reffuser;
    lokasi location;
    String idLokasi;
    int saldobayar;
    int waktutotal;
    int harga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        getSupportActionBar().setTitle("Detail Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Resources res = getApplicationContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("id")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        tvTempat=findViewById(R.id.txtDetailTempat);
        tvPlat=findViewById(R.id.txtDetailPlat);
        reff = FirebaseDatabase.getInstance().getReference();
        reffuser = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        tvJenis=findViewById(R.id.txtDetailJenis);
        tvKendaraan=findViewById(R.id.txtDetailKendaraan);
        tvSaldo = findViewById(R.id.txtSaldoBayar);
        tvDurasi=findViewById(R.id.txtDetailDurasi);
        tvJumlah=findViewById(R.id.txtDetailJumlah);
        email = getIntent().getStringExtra("email");
        jenis = getIntent().getStringExtra("jenis");
        tempat = getIntent().getStringExtra("tempat");
        platnomor = getIntent().getStringExtra("plat");
        durasi = getIntent().getStringExtra("durasi");
        kendaraan = getIntent().getStringExtra("kendaraan");
        idLokasi = getIntent().getStringExtra("idLokasi");
        reffuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saldobayar = Integer.parseInt(dataSnapshot.child("saldo").getValue().toString());
                tvSaldo.setText(saldobayar+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(jenis.equals("Titip")){
            waktu="hari";
            waktutotal=Integer.parseInt(durasi)*24;
            if(kendaraan.equals("Roda 2")){
                hargaRef = FirebaseDatabase.getInstance().getReference("Harga/Motor");
            }
            if(kendaraan.equals("Roda 4")){
                hargaRef = FirebaseDatabase.getInstance().getReference("Harga/Mobil");
            }
            hargaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    harga = Integer.parseInt(dataSnapshot.getValue().toString());
                    tvJumlah.setText(harga+"");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            harga = harga * Integer.parseInt(durasi);
        }
        if(jenis.equals("Parkir")){
            waktutotal=Integer.parseInt(durasi);
            waktu="jam";



            if(kendaraan.equals("Roda 2")){
                hargaRef = FirebaseDatabase.getInstance().getReference("Harga/Motor");
            }
            if(kendaraan.equals("Roda 4")){
                hargaRef = FirebaseDatabase.getInstance().getReference("Harga/Mobil");
            }

            hargaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    harga = Integer.parseInt(dataSnapshot.getValue().toString());
                    tvJumlah.setText(harga+"");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        tvJenis.setText(jenis+"");
        tvDurasi.setText(durasi+" "+waktu);
        tvTempat.setText(tempat+"");
        tvPlat.setText(platnomor+"");
        tvKendaraan.setText(kendaraan+"");
    }

    public void onClick(View view) {
        if(harga<=saldobayar){
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query1 = rootRef.child("Lokasi").child(idLokasi);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lokasi currLokasi = (lokasi)dataSnapshot.getValue();
                        if(kendaraan.equals("Roda 2"))
                        {
                            currLokasi.setSlotmotor(currLokasi.getSlotmotor() - 1);
                        }
                        else if(kendaraan.equals("Roda 4"))
                        {
                            currLokasi.setSlotmotor(currLokasi.getSlotmobil() - 1);
                        }
                        rootRef.child(idLokasi).setValue(currLokasi);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            query1.addListenerForSingleValueEvent(valueEventListener);

            ct=new classtransaksi(tempat+"",jenis+"",platnomor+"",kendaraan+"",waktutotal,harga,FirebaseAuth.getInstance().getCurrentUser().getUid()+"",strDate,durasi+" "+waktu,"Menunggu Konfirmasi Petugas");
            reff.child("currTransaksi").push().setValue(ct);
            reffuser.child("saldo").setValue(saldobayar-harga);
            Toast.makeText(this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DetailTransaksi.this,pembayaranberhasil.class);
            i.putExtra("harga",harga+"");
            startActivity(i);
            finish();
        }else{
            Toast.makeText(this, "Saldo Tidak Mencukupi", Toast.LENGTH_SHORT).show();
        }
    }
}
