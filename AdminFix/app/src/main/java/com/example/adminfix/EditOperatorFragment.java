package com.example.adminfix;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditOperatorFragment extends Fragment {

    EditText etNama,etNohp,etPassword;
    ArrayList<OperatorClass> listOperator = new ArrayList<OperatorClass>();
    ArrayList<LokasiClass> listLokasi = new ArrayList<LokasiClass>();
    Button btnEdit;
    TextView tvLokasi;

    Spinner spinnerEmail,spinnerLokasi;
    ArrayList<String> listEmail,listNamaLokasi,listIdLokasi,listIdOperator = new ArrayList<String>();
    ArrayAdapter<String> adapterEmail,adapterLokasi;

    MainActivity parentActivity;

    public EditOperatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_operator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting Components
        etNama = view.findViewById(R.id.etEditOwnerNama);
        etNohp = view.findViewById(R.id.etEditOwnerNohp);


        listEmail = new ArrayList<String>();
        listNamaLokasi = new ArrayList<String>();
        listIdLokasi = new ArrayList<String>();
        listIdOperator = new ArrayList<String>();

        spinnerEmail = view.findViewById(R.id.spinnerEditOwner);

        loadEmail();

        adapterEmail = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listEmail);
        adapterLokasi = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listNamaLokasi);

        //Setting Adapter to Spinners
        spinnerEmail.setAdapter(adapterEmail);

        parentActivity = (MainActivity)getActivity();

        btnEdit = view.findViewById(R.id.btnEditOperator);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOperator();
            }
        });

        spinnerEmail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshLabels();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                clearLabels();
            }
        });

        adapterEmail.notifyDataSetChanged();
        adapterLokasi.notifyDataSetChanged();
    }




    public void loadEmail()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Petugas");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempOperator = (HashMap<String, Object>)dataSnapshot.getValue();

                listEmail.clear();
                listOperator.clear();
                listIdOperator.clear();
                for (String key : tempOperator.keySet())
                {
                    listIdOperator.add(key);
                    System.out.println(tempOperator.get(key));
                    HashMap<String, String> tempObject = (HashMap<String,String>)tempOperator.get(key);
                    OperatorClass temporOperator = new OperatorClass();
                    temporOperator.setNama(tempObject.get("nama"));
                    temporOperator.setEmail(tempObject.get("email"));
                    temporOperator.setPassword(tempObject.get("password"));
                    temporOperator.setNohp(tempObject.get("nohp"));
                    temporOperator.setIdLokasi(tempObject.get("idlokasi"));

                    listOperator.add(temporOperator);
                    listEmail.add(temporOperator.getEmail());

                }
                adapterEmail = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listEmail);
                spinnerEmail.setAdapter(adapterEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void loadLokasi()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Lokasi");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();

                listLokasi.clear();
                listNamaLokasi.clear();
                listIdLokasi.clear();
                for (String key : tempLokasi.keySet())
                {
                    listIdLokasi.add(key);
                    System.out.println(tempLokasi.get(key));
                    HashMap<String, String> tempObject = (HashMap<String,String>)tempLokasi.get(key);
                    LokasiClass temporLokasi = new LokasiClass();
                    temporLokasi.setNama(tempObject.get("nama"));
                    temporLokasi.setPemilik(tempObject.get("pemilik"));

                    String hi = String.valueOf(tempObject.get("slotmobil"));

                    temporLokasi.setSlotmobil(Integer.parseInt(hi));
                    temporLokasi.setAlamat(tempObject.get("alamat"));

                    hi = String.valueOf(tempObject.get("lat"));
                    temporLokasi.setLat(Double.parseDouble(hi));

                    hi = String.valueOf(tempObject.get("lng"));
                    temporLokasi.setLng(Double.parseDouble(hi));

                    hi = String.valueOf(tempObject.get("slotmotor"));
                    temporLokasi.setSlotmotor(Integer.parseInt(hi));

                    listLokasi.add(temporLokasi);
                    listNamaLokasi.add(temporLokasi.getNama());

                }
                adapterLokasi = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listNamaLokasi);
                spinnerLokasi.setAdapter(adapterLokasi);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void editOperator()
    {
        OperatorClass tempOperator = listOperator.get(spinnerEmail.getSelectedItemPosition());
        tempOperator.setNama(etNama.getText().toString());
        tempOperator.setNohp(etNohp.getText().toString());
        //tempOperator.setPassword(etPassword.getText().toString());
        parentActivity.editOperator(listIdOperator.get(spinnerEmail.getSelectedItemPosition()),tempOperator);
        clearLabels();
    }

    public void refreshLabels()
    {
        OperatorClass tempOperator = listOperator.get(spinnerEmail.getSelectedItemPosition());
        etNama.setText(tempOperator.getNama()+"");
        etNohp.setText(tempOperator.getNohp()+"");
        //etPassword.setText(tempOperator.getPassword()+"");

        int ctr = 0;
        for(String id : listIdLokasi)
        {
            if(id.equals(tempOperator.getIdLokasi()))
            {
                tvLokasi.setText(listLokasi.get(ctr).getNama());
                break;
            }
            ctr++;
        }

        //tvLokasi.setText(tempOperator.getIdLokasi());
    }

    public void clearLabels()
    {
        etNama.setText("");
        etNohp.setText("");
        //etPassword.setText("");
        //tvLokasi.setText("-");
    }
}
