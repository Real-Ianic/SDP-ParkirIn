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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    BarChart barChart;

    ArrayList<Float> data1;
    ArrayList<Float> data2;

    List<BarEntry> listEntries;
    BarDataSet barDataSet;
    BarData barData;
    Description desc;

    ArrayList<classtransaksi> listTransaksi;
    ArrayList<String> listNamaLokasi;

    TaskCompletionSource<List<classtransaksi>> dbSource;
    Task dbTask;

    TaskCompletionSource<List<String>> dbSource2;
    Task dbTask2;

    Task allTask;

    MainActivity parentActivity;
    OwnerClass currentOwner;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting Views
        barChart = view.findViewById(R.id.barChart);

        //Prepping Variables
        data1 = new ArrayList<Float>();
        data2 = new ArrayList<Float>();

        listTransaksi = new ArrayList<>();
        listNamaLokasi = new ArrayList<>();




        //Prepping Data
        parentActivity = (MainActivity) getActivity();
        currentOwner = parentActivity.currentOwner;

        //Getting Data
        data1.add(1f);
        data1.add(2f);
        data1.add(3f);
        data1.add(4f);
        data1.add(5f);
        data1.add(6f);
        data1.add(7f);
        data1.add(8f);
        data1.add(9f);
        data1.add(10f);
        data1.add(11f);
        data1.add(12f);

        loadLokasi();
        getTransactions();


        allTask = Tasks.whenAll(dbTask,dbTask2);

        allTask.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                //Prepping Data After Async Task Finished
                System.out.println("MASUK GAN");

                HashMap<String,Integer> listPenghasilan = new HashMap<String,Integer>();

                listPenghasilan.put("Januari",0);
                listPenghasilan.put("Februari",0);
                listPenghasilan.put("Maret",0);
                listPenghasilan.put("April",0);
                listPenghasilan.put("Mei",0);
                listPenghasilan.put("Juni",0);
                listPenghasilan.put("Juli",0);
                listPenghasilan.put("Agustus",0);
                listPenghasilan.put("September",0);
                listPenghasilan.put("Oktober",0);
                listPenghasilan.put("November",0);
                listPenghasilan.put("Desember",0);

                for(int i=0;i<listTransaksi.size();i++)
                {
                    classtransaksi temp = listTransaksi.get(i);
                    String[] test = temp.getWaktutransaksi().split(" ");
                    String month = test[2];
                    int valueadd = temp.getTotal();

                    int value = listPenghasilan.get(month);
                    value = value + valueadd;

                    listPenghasilan.put(month,value);
                }

                data2.add((float)listPenghasilan.get("Januari"));
                data2.add((float)listPenghasilan.get("Februari"));
                data2.add((float)listPenghasilan.get("Maret"));
                data2.add((float)listPenghasilan.get("April"));
                data2.add((float)listPenghasilan.get("Mei"));
                data2.add((float)listPenghasilan.get("Juni"));
                data2.add((float)listPenghasilan.get("Juli"));
                data2.add((float)listPenghasilan.get("Agustus"));
                data2.add((float)listPenghasilan.get("September"));
                data2.add((float)listPenghasilan.get("Oktober"));
                data2.add((float)listPenghasilan.get("November"));
                data2.add((float)listPenghasilan.get("Desember"));

                //Setting BarChart
                listEntries = new ArrayList<BarEntry>();

                for(int i=0;i<data1.size();i++)
                {
                    if(i<data2.size())listEntries.add(new BarEntry(data1.get(i),data2.get(i)));
                    else listEntries.add(new BarEntry(data1.get(i),0));
                }

                barDataSet =new BarDataSet(listEntries,"Bulan");

                barData =new BarData(barDataSet);
                barData.setBarWidth(0.9f);

                barChart.setVisibility(View.VISIBLE);
                barChart.animateY(500);
                barChart.setData(barData);
                barChart.setFitBars(true);

                desc =new Description();
                desc.setText("Penghasilan per bulan");
                barChart.setDescription(desc);
                barChart.invalidate();
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
                System.out.println("dbTask Finished");
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
                System.out.println("dbTask2 Finished");
            }
        });
    }
}
