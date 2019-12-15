package com.example.adminfix;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
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
 * {@link EditUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Spinner userSpinner;
    ArrayAdapter<String> userAdapter;

    EditText edNama,edNohp,edAlamat;
    Button btnEditUser;

    List<Member> listUser;
    List<String> namaUser;
    List<String> idUser;

    public EditUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditUserFragment newInstance(String param1, String param2) {
        EditUserFragment fragment = new EditUserFragment();
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
        return inflater.inflate(R.layout.fragment_edit_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Prepping Variables
        listUser = new ArrayList<>();
        idUser = new ArrayList<>();
        namaUser = new ArrayList<>();


        //Getting Views
        userSpinner = view.findViewById(R.id.spinnerEditOwner);
        edNama = view.findViewById(R.id.etEditOwnerNama);
        edAlamat = view.findViewById(R.id.editText);
        edNohp = view.findViewById(R.id.etEditOwnerNohp);

        btnEditUser = view.findViewById(R.id.btnEditOwner);

        getUsers();

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser();
            }
        });
    }

    public void editUser()
    {
        String nama = edNama.getText().toString();
        String alamat = edAlamat.getText().toString();
        String nohp = edNohp.getText().toString();

        Member temp = listUser.get(userSpinner.getSelectedItemPosition());
        temp.setNama(nama);
        temp.setAlamat(alamat);
        temp.setNohp(nohp);

        MainActivity parent = (MainActivity) getActivity();
        parent.editUser(idUser.get(userSpinner.getSelectedItemPosition()),temp);
    }

    public void getUsers()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Member");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> obj = (HashMap<String,Object>)dataSnapshot.getValue();
                for (String key : obj.keySet())
                {
                    HashMap<String,Object> user = (HashMap<String,Object>)obj.get(key);
                    Member temp = new Member();
                    temp.setNama(user.get("nama").toString());
                    temp.setNohp(user.get("nohp").toString());
                    temp.setAlamat(user.get("alamat").toString());

                    int hi = Integer.parseInt(user.get("saldo").toString());

                    temp.setSaldo(hi);
                    temp.setEmail(user.get("email").toString());

                    listUser.add(temp);
                    namaUser.add(temp.getNama());
                    idUser.add(key);
                }
                userAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,namaUser);
                userSpinner.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
