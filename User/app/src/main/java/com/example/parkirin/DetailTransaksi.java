package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailTransaksi extends AppCompatActivity {

    TextView tvTempat,tvPlat,tvJenis,tvKendaraan,tvDurasi,tvJumlah,tvSaldo;
    String jenis,tempat,platnomor,durasi,kendaraan,waktu,email;
    private DatabaseReference reff;
    classtransaksi ct;
    Date currentTime = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    String strDate = dateFormat.format(currentTime);
    DatabaseReference reffuser;
    int saldobayar;
    int waktutotal;
    int harga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        getSupportActionBar().setTitle("Detail Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                harga=12000*Integer.parseInt(durasi);
            }
            if(kendaraan.equals("Roda 4")){
                harga=25000*Integer.parseInt(durasi);
            }
        }
        if(jenis.equals("Parkir")){
            waktutotal=Integer.parseInt(durasi);
            waktu="jam";
            if(kendaraan.equals("Roda 2")){
                harga=10000;
            }
            if(kendaraan.equals("Roda 4")){
                harga=20000;
            }
        }
        tvJenis.setText(jenis+"");
        tvDurasi.setText(durasi+" "+waktu);
        tvTempat.setText(tempat+"");
        tvPlat.setText(platnomor+"");
        tvKendaraan.setText(kendaraan+"");
        tvJumlah.setText(harga/1000+".000");
    }

    public void onClick(View view) {
        if(harga<=saldobayar){
            ct=new classtransaksi(tempat+"",jenis+"",platnomor+"",kendaraan+"",waktutotal,harga,email+"",strDate,durasi+" "+waktu,"Menunggu Konfirmasi Petugas");
            reff.child("Transaksi").push().setValue(ct);
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
