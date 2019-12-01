package com.example.adminfix;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddLokasiFragment extends Fragment {
    EditText nama,alamat,pemilik,slotmotor,slotmobil,lat,lng;
    String sNama;
    String sAlamat;
    String sPemilik;
    double sLat;
    double sLng;
    int sMotor,sMobil;
    DatabaseReference reff;
    Button add;
    LokasiClass l;

    public AddLokasiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nama=view.findViewById(R.id.etNamaLokasi);
        add=view.findViewById(R.id.btnTambahLokasi);
        alamat=view.findViewById(R.id.etAlamatLokasi);
        slotmobil=view.findViewById(R.id.etSlotmobil);
        slotmotor=view.findViewById(R.id.etSlotmotor);
        pemilik=view.findViewById(R.id.etPemilik);
        lat=view.findViewById(R.id.etLatitude);
        lng=view.findViewById(R.id.etLongitude);
        reff= FirebaseDatabase.getInstance().getReference();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nama.getText().toString())){
                    nama.setError("Nama Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(alamat.getText().toString())){
                    alamat.setError("Alamat Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(slotmobil.getText().toString())){
                    slotmobil.setError("Slot Mobil Harus Diisi");
                }
                if(TextUtils.isEmpty(slotmotor.getText().toString())){
                    slotmotor.setError("Slot Motor Harus Diisi");
                }
                if(TextUtils.isEmpty(pemilik.getText().toString())){
                    pemilik.setError("Pemilik Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(lat.getText().toString())){
                    lat.setError("Latitude Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(lng.getText().toString())){
                    lng.setError("Longitude Lokasi Harus Diisi");
                }
                if(!(nama.getText().toString().isEmpty()||alamat.getText().toString().isEmpty()||slotmotor.getText().toString().isEmpty()||pemilik.getText().toString().isEmpty()||slotmobil.getText().toString().isEmpty()||lat.getText().toString().isEmpty()||lng.getText().toString().isEmpty())){
                    sNama=nama.getText().toString();
                    sAlamat=alamat.getText().toString();
                    sPemilik=pemilik.getText().toString();
                    sMobil=Integer.parseInt(slotmobil.getText().toString());
                    sMotor=Integer.parseInt(slotmotor.getText().toString());
                    sLat=Double.parseDouble(lat.getText().toString());
                    sLng=Double.parseDouble(lng.getText().toString());
                    Query query = reff.child("Lokasi").orderByChild("nama").equalTo(sNama);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getActivity(), "Lokasi Sudah Ada", Toast.LENGTH_SHORT).show();
                            }else{
                                l = new LokasiClass(sNama,sAlamat,sPemilik,sLat,sLng,sMobil,sMotor);
                                reff.child("Lokasi").push().setValue(l);
                                Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                                nama.setText("");
                                alamat.setText("");
                                pemilik.setText("");
                                slotmotor.setText("");
                                slotmobil.setText("");
                                lat.setText("");
                                lng.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }else{
                    Toast.makeText(getContext(), "Semua Data Wajib Diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_lokasi, container, false);
    }

}
