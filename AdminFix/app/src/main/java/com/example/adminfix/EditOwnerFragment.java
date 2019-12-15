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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditOwnerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditOwnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditOwnerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Spinner spinnerOwner;
    ArrayAdapter<String> adapterOwner;
    List<String> namaOwner;
    List<String> idOwner;

    EditText edNama,edNohp;

    Button btnEdit;


    List<OwnerClass> listOwner = new ArrayList<>();

    public EditOwnerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditOwnerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditOwnerFragment newInstance(String param1, String param2) {
        EditOwnerFragment fragment = new EditOwnerFragment();
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
        return inflater.inflate(R.layout.fragment_edit_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        namaOwner = new ArrayList<>();
        idOwner = new ArrayList<>();

        spinnerOwner = view.findViewById(R.id.spinnerEditOwner);
        edNama = view.findViewById(R.id.etEditOwnerNama);
        edNohp = view.findViewById(R.id.etEditOwnerNohp);

        btnEdit = view.findViewById(R.id.btnEditOperator);
        getOwner();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOwner();
            }
        });
    }

    public void editOwner()
    {
        int pos = spinnerOwner.getSelectedItemPosition();
        String name = edNama.getText().toString();
        String nohp = edNohp.getText().toString();
        MainActivity parent = (MainActivity) getActivity();
        OwnerClass hi = listOwner.get(pos);
        hi.setName(name);
        hi.setNohp(nohp);
        parent.editOwner(idOwner.get(pos),hi);
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

    public void getOwner()
    {
        listOwner.clear();
        idOwner.clear();
        namaOwner.clear();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Owner");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> obj = (HashMap<String,Object>) dataSnapshot.getValue();

                for(String key : obj.keySet())
                {
                    idOwner.add(key);
                    HashMap<String,Object> owner = (HashMap<String,Object>)obj.get(key);
                    OwnerClass tempOwner = new OwnerClass();
                    tempOwner.setNohp(owner.get("Nohp").toString());
                    tempOwner.setEmail(owner.get("email").toString());
                    tempOwner.setPassword(owner.get("password").toString());
                    tempOwner.setName(owner.get("name").toString());

                    listOwner.add(tempOwner);
                    namaOwner.add(tempOwner.getName());
                }
                adapterOwner = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,namaOwner);
                spinnerOwner.setAdapter(adapterOwner);
                adapterOwner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
