package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class pembayaranberhasil extends AppCompatActivity {

    TextView status;
    String harga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaranberhasil);
        status = findViewById(R.id.txtBerhasil);
        harga = getIntent().getStringExtra("harga");
        int intharga =Integer.parseInt(harga);
        if (intharga>999 && intharga<1000000){
            harga = (intharga/1000)+".000";
        }
        else if (intharga>=1000000){
            harga = (intharga/1000000)+".000";
        }
        status.setText("Pembayaran dengan CashIn senilai Rp "+harga+",00 telah berhasil");
    }

    public void home(View view) {
        finish();
    }
}
