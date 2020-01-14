package com.example.adminfix;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HargaFragment extends Fragment {

    EditText etHargaMobil;
    EditText etHargaMotor;
    Button btnSaveHarga;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference motorRef = database.getReference("Harga/Motor");
    DatabaseReference mobilRef = database.getReference("Harga/Mobil");

    public HargaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_harga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting Components

        etHargaMobil = view.findViewById(R.id.etHargaMobil);
        etHargaMotor = view.findViewById(R.id.etHargaMotor);
        btnSaveHarga = view.findViewById(R.id.btnHargaSave);

        btnSaveHarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHarga();
            }
        });

        loadHarga();
    }

    public void loadHarga()
    {
        DatabaseReference ref = database.getReference("Harga");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> obj = (HashMap<String,Object>) dataSnapshot.getValue();

                    for(String key : obj.keySet())
                    {
                        if(key.equals("Mobil"))
                        {
                            etHargaMobil.setText(obj.get(key).toString());
                        }
                        else
                        {
                            etHargaMotor.setText(obj.get(key).toString());
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveHarga()
    {
        try {
            int hargaMobil = Integer.parseInt(etHargaMobil.getText().toString());
            int hargaMotor = Integer.parseInt(etHargaMotor.getText().toString());

            motorRef.setValue(hargaMotor);
            mobilRef.setValue(hargaMobil);
        }
        catch(Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            Toast.makeText(getContext(), "Berhasil Simpan !", Toast.LENGTH_SHORT).show();
        }
    }
}
