package com.example.adminfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;

public class AddLokasiActivity extends AppCompatActivity {
    EditText nama,alamat,pemilik,slot;
    String sNama,sAlamat,sPemilik;
    DatabaseReference reff;
    int jumlahSlot;
    Button add;
    LokasiClass l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lokasi);
        nama=findViewById(R.id.namaLokasi);
        add=findViewById(R.id.btnTambahLokasi);
        alamat=findViewById(R.id.alamatLokasi);
        slot=findViewById(R.id.jumlahSlot);
        pemilik=findViewById(R.id.pemilikLokasi);
        l = new LokasiClass();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reff= FirebaseDatabase.getInstance().getReference().child("Lokasi");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nama.getText().toString())){
                    nama.setError("Nama Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(alamat.getText().toString())){
                    alamat.setError("Alamat Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(slot.getText().toString())){
                    slot.setError("Slot Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(pemilik.getText().toString())){
                    pemilik.setError("Pemilik Lokasi Harus Diisi");
                }
                if(!(nama.getText().toString().isEmpty()||alamat.getText().toString().isEmpty()||slot.getText().toString().isEmpty()||pemilik.getText().toString().isEmpty())){
                    sNama=nama.getText().toString();
                    sAlamat=alamat.getText().toString();
                    sPemilik=pemilik.getText().toString();
                    jumlahSlot=Integer.parseInt(slot.getText().toString());
                    l.setNama(sNama);
                    l.setAlamat(sAlamat);
                    l.setPemilik(sPemilik);
                    l.setSlot(jumlahSlot);
                    reff.push().setValue(l);
                }
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
