package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pemesananActivity extends AppCompatActivity {

    TextView txttempat;
    RadioButton titip,parkir,roda2,roda4;
    EditText etdurasi,etplatnomordepan,etplatnomorangka,getEtplatnomorbelakang;
    String platnomor,jenis,kendaraan,tempat,durasi;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        getSupportActionBar().setTitle("Pemesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txttempat = findViewById(R.id.txtTempat);
        email = getIntent().getStringExtra("email");
        titip = findViewById(R.id.radioHari);
        parkir = findViewById(R.id.radioParkir);
        roda2 = findViewById(R.id.radioMotor);
        roda4 = findViewById(R.id.radioMobil);
        etdurasi = findViewById(R.id.txtDurasipemesanan);
        etplatnomordepan = findViewById(R.id.txtPlatnomorDepan);
        etplatnomorangka = findViewById(R.id.txtPlatnomorAngka);
        getEtplatnomorbelakang = findViewById(R.id.txtPlatnomorBelakang);
        tempat = getIntent().getStringExtra("tempat");
        txttempat.setText(tempat);
    }

    public void onClick(View view) {
        if(titip.isChecked()){
            jenis="Titip";
        }
        if(parkir.isChecked()){
            jenis="Parkir";
        }
        if(roda2.isChecked()){
            kendaraan="Roda 2";
        }
        if(roda4.isChecked()){
            kendaraan="Roda 4";
        }

        if(TextUtils.isEmpty(etdurasi.getText().toString())){
            etdurasi.setError("Durasi Harus Diisi");
        }
        if(TextUtils.isEmpty(etplatnomordepan.getText().toString())||TextUtils.isEmpty(etplatnomorangka.getText().toString())){
            getEtplatnomorbelakang.setError("Plat Nomor Harus Diisi");
        }
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(etplatnomordepan.getText().toString());
        boolean bs = ms.matches();
        if(etplatnomordepan.length()>2||bs==false){
            etplatnomordepan.setError("Format Plat Nomor Tidak Sesuai");
        }
        if(etplatnomorangka.length()>4){
            etplatnomorangka.setError("Format Plat Nomor Tidak Sesuai");
        }
        if(getEtplatnomorbelakang.length()>3){
            getEtplatnomorbelakang.setError("Format Plat Nomor Tidak Sesuai");
        }
        if(!etdurasi.getText().toString().equals("")&&!etplatnomordepan.getText().toString().equals("")&&!etplatnomorangka.getText().toString().equals("")&&etplatnomordepan.getText().toString().length()<=2&&etplatnomorangka.getText().toString().length()<=4&&getEtplatnomorbelakang.getText().toString().length()<=3&&bs==true){
            platnomor = etplatnomordepan.getText().toString().toUpperCase()+" "+etplatnomorangka.getText().toString()+" "+getEtplatnomorbelakang.getText().toString().toUpperCase();
            durasi = etdurasi.getText().toString();
            Intent i = new Intent(this,DetailTransaksi.class);
            i.putExtra("jenis",jenis);
            i.putExtra("kendaraan",kendaraan);
            i.putExtra("plat",platnomor);
            i.putExtra("durasi",durasi);
            i.putExtra("tempat",tempat);
            i.putExtra("email",email);
            startActivity(i);
            finish();
        }else {
            Toast.makeText(this, "Semua field harus diisi dengan benar!", Toast.LENGTH_SHORT).show();
        }
    }
}
