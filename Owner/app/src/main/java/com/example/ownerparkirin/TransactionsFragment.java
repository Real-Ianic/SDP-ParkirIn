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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
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
    ArrayList<classtransaksi> allListTransaksi;
    ArrayList<String> listNamaLokasi;

    TaskCompletionSource<ArrayList<classtransaksi>> dbSource;
    Task dbTask;

    TaskCompletionSource<ArrayList<String>> dbSource2;
    Task dbTask2;

    Task allTask;

    OwnerClass currentOwner;


    EditText searchBar;
    Spinner spinnerBar;

    ArrayAdapter<String> adapterSpinner;

    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    public void refreshList(String lokasi)
    {
        if(lokasi.equals(""))
        {

        }
        else
        {
            System.out.println("List : " + listTransaksi);
            System.out.println("All List " + allListTransaksi);
            listTransaksi.clear();

            for(int i=0;i<allListTransaksi.size();i++)
            {
                if(allListTransaksi.get(i).getTempat().equals(lokasi))
                {
                    listTransaksi.add(allListTransaksi.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listTransaksi = new ArrayList<>();
        listNamaLokasi = new ArrayList<>();

        parentActivity = (MainActivity) getActivity();
        currentOwner = parentActivity.currentOwner;
        rvTrans = view.findViewById(R.id.rvTrans);
        //spinnerBar = view.findViewById(R.id.spinnerTransactionSearch);
        getTransactions();
        loadLokasi();
        allTask = Tasks.whenAll(dbTask,dbTask2);
        allTask.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                //String lokasi = spinnerBar.getSelectedItem().toString();
               // System.out.println("List Transaksi OnComplete : " + listTransaksi);
                //refreshList(lokasi);

                //System.out.println("ONALLCOMPLETE : " + listTransaksi);
                //System.out.println("ONALLCOMPLETE : " + allListTransaksi);

                for(int i=0;i<listTransaksi.size();i++)
                {
                    boolean found = false;
                    for(int j=0;j<listNamaLokasi.size();j++)
                    {
                        if(listTransaksi.get(i).getTempat().equals(listNamaLokasi.get(j)))
                        {
                            found = true;
                            break;
                        }
                    }

                    if(!found)
                    {
                        listTransaksi.remove(i);
                        i--;
                    }
                }

                adapter.notifyDataSetChanged();

                /*
                spinnerBar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String lokasi = spinnerBar.getSelectedItem().toString();
                        refreshList(lokasi);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });*/
            }
        });
    }

    public void getTransactions()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Transaksi");

        dbSource = new TaskCompletionSource<>();
        dbTask = dbSource.getTask();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();
                listTransaksi.clear();

                for (String key : tempLokasi.keySet())
                {
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
                dbSource.setResult(listTransaksi);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                dbSource.setException(databaseError.toException());
            }
        });

        dbTask.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                adapter = new TransaksiAdapter(listTransaksi);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                rvTrans.setAdapter(adapter);
                rvTrans.setLayoutManager(lm);
                adapter.notifyDataSetChanged();
                System.out.println("HALO : "+listTransaksi);
                allListTransaksi = listTransaksi;
                System.out.println("HAI : "+allListTransaksi);
            }
        });
    }

    public void loadLokasi()
    {
        dbSource2 = new TaskCompletionSource<>();
        dbTask2 = dbSource2.getTask();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Lokasi");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();

                listNamaLokasi.clear();
                for (String key : tempLokasi.keySet())
                {
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

                    if(temporLokasi.getPemilik().equals(currentOwner.getEmail()))listNamaLokasi.add(temporLokasi.getNama());
                }
                dbSource2.setResult(listNamaLokasi);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                dbSource2.setException(databaseError.toException());
            }
        });

        dbTask2.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                /*adapterSpinner = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listNamaLokasi);
                spinnerBar.setAdapter(adapterSpinner);
                adapterSpinner.notifyDataSetChanged();*/
            }
        });
    }
}
