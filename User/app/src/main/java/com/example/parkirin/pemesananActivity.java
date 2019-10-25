package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class pemesananActivity extends AppCompatActivity {
    TextView tempat;
    EditText durasi;
    RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        getSupportActionBar().setTitle("Pemesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tempat=findViewById(R.id.txtTempat);
        durasi=findViewById(R.id.editText);
        rg=findViewById(R.id.radio);
        tempat.setText(getIntent().getStringExtra("tempat")+"");
    }

    public void onClick(View view) {
        String durtemp = durasi.getText().toString();
        if(durtemp.isEmpty()){
            durasi.setError("Durasi Harus Diisi");
        }
        else if(Integer.parseInt(durtemp.toString())<=0){
            durasi.setError("Durasi Minimal 1 Jam");
        }
        else{
            Intent i = new Intent(this,DetailTransaksi.class);
            finish();
            startActivity(i);
        }
    }
}
