package com.example.ownerparkirin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
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
public class YourLocationsFragment extends Fragment {

    TaskCompletionSource<List<LokasiClass>> dbSource;
    Task dbTask;

    OwnerClass currentOwner;
    MainActivity parentActivity;

    List<LokasiClass> listLokasi = new ArrayList<>();
    List<String> listNamaLokasi = new ArrayList<>();

    public YourLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_locations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Constructor Here

        parentActivity = (MainActivity) getActivity();
        currentOwner = parentActivity.currentOwner;
    }

    private void loadLokasi()
    {
        dbSource = new TaskCompletionSource<>();
        dbTask = dbSource.getTask();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Lokasi");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> tempLokasi = (HashMap<String,Object>)dataSnapshot.getValue();
                listLokasi.clear();
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

                    listLokasi.add(temporLokasi);
                    listNamaLokasi.add(temporLokasi.getNama());
                }
                dbSource.setResult(listLokasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dbSource.setException(databaseError.toException());
            }
        });

        dbTask.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                for(int i=0;i<listLokasi.size();i++)
                {
                    if(!listLokasi.get(i).getPemilik().equals(currentOwner.getEmail()))
                    {
                        listLokasi.remove(i);
                        listNamaLokasi.remove(i);
                        i--;
                    }
                }
            }
        });
    }
}
