package com.example.adminfix;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddOperatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddOperatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOperatorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    List<LokasiClass> listLokasi = new ArrayList<LokasiClass>();
    Spinner spinnerLokasiOperator;
    ArrayList<String> listNamaLokasi = new ArrayList<String>();
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> listIdLokasi = new ArrayList<>();

    EditText etNama,etEmail,etPassword,etNohp;
    Button btnRegisterOperator;
    FirebaseAuth mAuth;

    public AddOperatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOperatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOperatorFragment newInstance(String param1, String param2) {
        AddOperatorFragment fragment = new AddOperatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_operator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,listNamaLokasi);
        mAuth = FirebaseAuth.getInstance();
        etNama = view.findViewById(R.id.edNamaOperator);
        etEmail = view.findViewById(R.id.edEmailOperator);
        etPassword = view.findViewById(R.id.edPasswordOperator);
        etNohp = view.findViewById(R.id.edNoHPOperator);
        btnRegisterOperator = view.findViewById(R.id.btnTambahOperator);

        btnRegisterOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOperator();
            }
        });

        spinnerLokasiOperator = view.findViewById(R.id.spinnerLokasiOperator);
        spinnerLokasiOperator.setAdapter(spinnerAdapter);
        loadLocations();
        spinnerAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
 */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Added Functions

    public void loadLocations()
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

                    System.out.println("=============");
                    System.out.println(temporLokasi);

                    listLokasi.add(temporLokasi);
                    listNamaLokasi.add(temporLokasi.getNama());

                    /*
                    Object data = tempLokasi.get(key);
                    LokasiClass temp = (LokasiClass) data;
                    try{
                        listLokasi.add((LokasiClass) data);
                        listNamaLokasi.add(temp.getNama());
                    }
                    catch(ClassCastException cce){
                    }
                     */
                    spinnerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    OperatorClass tempOperator;
    public void registerOperator()
    {
        final String name = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String nohp = etNohp.getText().toString();

        final String idLokasi = listIdLokasi.get(spinnerLokasiOperator.getSelectedItemPosition());
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    tempOperator = new OperatorClass(name,email,password,nohp,idLokasi);
                    FirebaseDatabase.getInstance().getReference("Petugas").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(tempOperator).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(),"Register Petugas Berhasil",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        MainActivity parentActivity = (MainActivity)this.getActivity();
        parentActivity.addOperator(tempOperator);
        clearForm();
    }

    public void clearForm()
    {
        etNama.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etNohp.setText("");
    }
}