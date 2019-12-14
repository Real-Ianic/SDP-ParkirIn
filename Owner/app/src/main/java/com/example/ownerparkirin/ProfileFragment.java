package com.example.ownerparkirin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    TextView tvemail;
    EditText ednama, ednohp,edpass;
    Button btnsave;

    MainActivity parentActivity;

    String email, statuskomisi, nama;
    int nohp;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Getting Components
        tvemail = view.findViewById(R.id.tvProfileEmail);
        ednama = view.findViewById(R.id.edProfileNama);
        ednohp = view.findViewById(R.id.edProfileNohp);
        edpass = view.findViewById(R.id.edProfilePass);

        btnsave = view.findViewById(R.id.btnProfileSave);

        tvemail.setText(parentActivity.currentOwner.getEmail());

        parentActivity = (MainActivity) getActivity();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = tvemail.getText().toString();
                String nama = ednama.getText().toString();
                String nohp = ednohp.getText().toString();
                String pass = edpass.getText().toString();

                OwnerClass tempOwner = new OwnerClass();
                tempOwner.setEmail(email);
                tempOwner.setName(nama);
                tempOwner.setNohp(nohp);
                tempOwner.setPassword(pass);

                parentActivity.editOwner(tempOwner);
            }
        });
    }

}
