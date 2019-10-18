package com.example.parkirin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoritesFragment extends Fragment {
    Button s,l,sk,topup;
    EditText etNominal;
    TextView txSaldo;
    int saldo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        s=view.findViewById(R.id.btn10k);
        l=view.findViewById(R.id.btn50k);
        sk=view.findViewById(R.id.btn100k);
        topup=view.findViewById(R.id.btnTopUp);
        etNominal=view.findViewById(R.id.etNominal);
        txSaldo=view.findViewById(R.id.txtSaldo);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("10000");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("50000");
            }
        });
        sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("100000");
            }
        });
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etNominal.getText().toString())){
                    etNominal.setError( "Jumlah harus diisi!" );
                }else{
                    saldo+=Integer.parseInt(etNominal.getText().toString());
                    txSaldo.setText(saldo+"");
                }
            }
        });
    }
}
