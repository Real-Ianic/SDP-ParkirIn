package com.example.parkirin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class FavoritesFragment extends Fragment {
    Button s,l,sk,topup;
    EditText etNominal;
    TextView txSaldo;
    DatabaseReference reff;
    int saldotopup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saldotopup = Integer.parseInt(dataSnapshot.child("saldo").getValue().toString());
                txSaldo.setText(saldotopup+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            getActivity().finish();
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        s=view.findViewById(R.id.btn10k);
        l=view.findViewById(R.id.btn50k);
        sk=view.findViewById(R.id.btn100k);
        topup=view.findViewById(R.id.btnTopUp);
        etNominal=view.findViewById(R.id.etNominal);
        txSaldo=view.findViewById(R.id.txtSaldoTopUp);
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
                    reff.child("saldo").setValue(saldotopup+Integer.parseInt(etNominal.getText().toString()));
                }
            }
        });
    }
}
