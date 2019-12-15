package com.example.ownerparkirin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 */
public class TransactionsFragment extends Fragment {

    RecyclerView rvTrans;
    TransaksiAdapter adapter;

    MainActivity parentActivity;

    ArrayList<classtransaksi> listTransaksi;

    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentActivity = (MainActivity) getActivity();

        rvTrans = view.findViewById(R.id.rvTrans);
        adapter = new TransaksiAdapter(listTransaksi);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());

        rvTrans.setAdapter(adapter);
        rvTrans.setLayoutManager(lm);
        getTransactions();
    }

    public void getTransactions()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Transaksi");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();
                listTransaksi.clear();

                for (String key : tempLokasi.keySet())
                {
                    System.out.println("Key : " + key);
                    HashMap<String,Object> transaksi = (HashMap<String,Object>) tempLokasi.get(key);
                    classtransaksi trans = new classtransaksi();

                    String hi = String.valueOf(transaksi.get("durasijam"));

                    trans.setDurasijam(Integer.parseInt(hi));
                    trans.setDurasiutktampil(transaksi.get("durasiutktampil").toString());

                    trans.setEmailcust(transaksi.get("emailcust").toString());
                    trans.setJenis(transaksi.get("jenis").toString());
                    trans.setPlatnomor(transaksi.get("platnomor").toString());
                    trans.setStatus(transaksi.get("status").toString());
                    trans.setTempat(transaksi.get("tempat").toString());
                    trans.setTipekendaraan(transaksi.get("tipekendaraan").toString());

                    hi = String.valueOf(transaksi.get("total"));

                    trans.setTotal(Integer.parseInt(hi));
                    trans.setWaktutransaksi(transaksi.get("waktutransaksi").toString());

                    listTransaksi.add(trans);
                }
                adapter = new TransaksiAdapter(listTransaksi);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                rvTrans.setAdapter(adapter);
                rvTrans.setLayoutManager(lm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
