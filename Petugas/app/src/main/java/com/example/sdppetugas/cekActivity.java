package com.example.sdppetugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class cekActivity extends AppCompatActivity {
    ArrayList<String> arrOnGoing,arrPlat;
    ArrayAdapter<String> platAdapter;
    ListView lvPlat;
    TextView tvPlat,tvNama,tvDurasi,tvHarga;
    Button btnCekOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek);
        lvPlat = findViewById(R.id.lvPlat);
        tvNama = findViewById(R.id.tvNamaPlat);
        tvPlat = findViewById(R.id.tvPlatNo);
        tvDurasi = findViewById(R.id.tvDurasi);
        tvHarga = findViewById(R.id.tvHarga);
        btnCekOut = findViewById(R.id.btnCekOut);
        tvDurasi.setText("");
        tvHarga.setText("");
        arrOnGoing = new ArrayList<String>();
        arrPlat = new ArrayList<String>();
       if(getIntent()!=null){
            arrOnGoing = getIntent().getStringArrayListExtra("ongoing");
            arrPlat = getIntent().getStringArrayListExtra("platno");
        }
        platAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrPlat);
        lvPlat.setAdapter(platAdapter);
        lvPlat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvNama.setText(arrOnGoing.get(i));
                tvPlat.setText(arrPlat.get(i));
                tvDurasi.setText("100 menit");
                tvHarga.setText("100.000");
            }
        });
        btnCekOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int a = 0; a < arrOnGoing.size(); a++) {
                    if(arrOnGoing.get(a).equals(tvNama.getText().toString())){
                        arrOnGoing.remove(a);
                        arrPlat.remove(a);
                    }
                }
                platAdapter.notifyDataSetChanged();
                tvNama.setText("");
                tvPlat.setText("");
                tvDurasi.setText("");
                tvHarga.setText("");
            }
        });
    }
    public void cekKendaraan(View v){
        Intent i = new Intent(this,statusKendaraan.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.optLogout){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
