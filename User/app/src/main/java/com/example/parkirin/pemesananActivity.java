package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pemesananActivity extends AppCompatActivity {

    TextView txttempat,txtKeterangan;
    RadioButton titip,parkir,roda2,roda4;
    RadioGroup rg;
    EditText etdurasi,etplatnomordepan,etplatnomorangka,getEtplatnomorbelakang;
    String platnomor,jenis,kendaraan,tempat,durasi;
    String email;
    String idLokasi;
    int slotMobil;
    int slotMotor;
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
        txtKeterangan = findViewById(R.id.keterangan);
        rg = findViewById(R.id.radioJenis);
        roda2 = findViewById(R.id.radioMotor);
        roda4 = findViewById(R.id.radioMobil);
        etdurasi = findViewById(R.id.txtDurasipemesanan);
        etplatnomordepan = findViewById(R.id.txtPlatnomorDepan);
        etplatnomorangka = findViewById(R.id.txtPlatnomorAngka);
        getEtplatnomorbelakang = findViewById(R.id.txtPlatnomorBelakang);
        idLokasi = getIntent().getStringExtra("idLokasi");
        tempat = getIntent().getStringExtra("tempat");
        slotMobil = Integer.parseInt(getIntent().getStringExtra("slotMobil"));
        slotMotor = Integer.parseInt(getIntent().getStringExtra("slotMotor"));
        txttempat.setText(tempat);
        titip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etdurasi.setText("24");
                etdurasi.setEnabled(false);
                txtKeterangan.setText("Jam");
            }
        });
        parkir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etdurasi.setText("");
                etdurasi.setEnabled(true);
                txtKeterangan.setText("Hari");
            }
        });
    }

//    RadioGroup rGroup = (RadioGroup)findViewById(R.id.radioJenis);
//    // This will get the radiobutton in the radiogroup that is checked
//    RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());

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

        if(etdurasi.getText().toString().equals("0")||etdurasi.getText().toString().equals("1")){
            etdurasi.setError("Penitipan Harus Lebih Dari 1 Hari");
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
        if(!etdurasi.getText().toString().equals("")&&!etplatnomordepan.getText().toString().equals("")
                &&!etplatnomorangka.getText().toString().equals("")&&etplatnomordepan.getText().toString().length()<=2
                &&etplatnomorangka.getText().toString().length()<=4&&getEtplatnomorbelakang.getText().toString().length()<=3&&bs==true
        &&!etdurasi.getText().toString().equals("0")&&!etdurasi.getText().toString().equals("1")){
            if(kendaraan.equals("Roda 2"))
            {
                if(slotMotor - 1 >= 0)
                {
                    platnomor = etplatnomordepan.getText().toString().toUpperCase()+" "+etplatnomorangka.getText().toString()+" "+getEtplatnomorbelakang.getText().toString().toUpperCase();
                    durasi = etdurasi.getText().toString();
                    Intent i = new Intent(this,DetailTransaksi.class);
                    i.putExtra("jenis",jenis);
                    i.putExtra("kendaraan",kendaraan);
                    i.putExtra("plat",platnomor);
                    i.putExtra("durasi",durasi);
                    i.putExtra("tempat",tempat);
                    i.putExtra("email",email);
                    i.putExtra("idLokasi",idLokasi);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Maaf, Kuota motor penuh", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if(slotMobil - 1 >= 0)
                {
                    platnomor = etplatnomordepan.getText().toString().toUpperCase()+" "+etplatnomorangka.getText().toString()+" "+getEtplatnomorbelakang.getText().toString().toUpperCase();
                    durasi = etdurasi.getText().toString();
                    Intent i = new Intent(this,DetailTransaksi.class);
                    i.putExtra("jenis",jenis);
                    i.putExtra("kendaraan",kendaraan);
                    i.putExtra("plat",platnomor);
                    i.putExtra("durasi",durasi);
                    i.putExtra("tempat",tempat);
                    i.putExtra("email",email);
                    i.putExtra("idLokasi",idLokasi);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Maaf, Kuota mobil penuh", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(this, "Semua field harus diisi dengan benar!", Toast.LENGTH_SHORT).show();
        }
    }
}
