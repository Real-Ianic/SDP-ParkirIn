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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditOperatorFragment extends Fragment {

    EditText etNama,etNohp,etPassword;
    List<OperatorClass> listOperator = new ArrayList<OperatorClass>();
    List<LokasiClass> listLokasi = new ArrayList<LokasiClass>();
    Button btnEdit;

    Spinner spinnerEmail,spinnerLokasi;
    List<String> listEmail,listNamaLokasi = new ArrayList<String>();
    ArrayAdapter<String> adapterEmail,adapterLokasi;

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
        etNama = view.findViewById(R.id.etEditOperatorName);
        etNohp = view.findViewById(R.id.etEditOperatorNohp);
        etPassword = view.findViewById(R.id.etEditOperatorPassword);

        spinnerEmail = view.findViewById(R.id.spinnerEditOperatorEmail);
        spinnerLokasi = view.findViewById(R.id.spinnerEditOperatorLokasi);

        adapterEmail = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,listEmail);
        adapterLokasi = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,listNamaLokasi);

        //Setting Adapter to Spinners
        spinnerEmail.setAdapter(adapterEmail);
        spinnerLokasi.setAdapter(adapterLokasi);

        btnEdit = view.findViewById(R.id.btnEditOperator);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOperator();
            }
        });
    }

    public void loadLokasi()
    {

    }

    public void loadEmail()
    {

    }

    public void editOperator()
    {

    }
}
