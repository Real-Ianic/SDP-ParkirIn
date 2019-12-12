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

import java.util.ArrayList;
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
        listTransaksi = (ArrayList<classtransaksi>)parentActivity.getTransactions();
        adapter.notifyDataSetChanged();
    }
}
