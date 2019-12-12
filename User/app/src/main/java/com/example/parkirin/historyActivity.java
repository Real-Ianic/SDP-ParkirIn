package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class historyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private RecyclerView rvHistory;
    private ArrayList<classtransaksi> listTrans = new ArrayList<classtransaksi>();
    private FirebaseRecyclerOptions<classtransaksi> options;
    private DatabaseReference databaseReference;
    private historyAdapter adapter,adapter2;
    private String email;
    private EditText etFilter;
    private Button btnFilter;

    @Override
    protected void onStart() {
        super.onStart();
        Resources res = getApplicationContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("id")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Resources res = getApplicationContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("id")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        etFilter = findViewById(R.id.etFilter);
        btnFilter = findViewById(R.id.btnFilter);
        getSupportActionBar().setTitle("Histori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email=getIntent().getStringExtra("email");
        rvHistory = findViewById(R.id.rvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        etFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        Query query = FirebaseDatabase.getInstance().getReference("Transaksi").orderByChild("emailcust").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    classtransaksi ct = ds.getValue(classtransaksi.class);
                    listTrans.add(ct);
                }
                adapter = new historyAdapter(listTrans);
                rvHistory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTrans.clear();
                Query query = FirebaseDatabase.getInstance().getReference("Transaksi").orderByChild("emailcust").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
                query.keepSynced(true);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            classtransaksi ct = ds.getValue(classtransaksi.class);
                            listTrans.add(ct);
                        }
                        adapter = new historyAdapter(listTrans);
                        rvHistory.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void showDatePickerDialog(){
        Resources res = getApplicationContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("id")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listTrans.clear();
        Resources res = getApplicationContext().getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("id")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        Calendar calenda = Calendar.getInstance();
        calenda.set(year,month,dayOfMonth);
        Date date = calenda.getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String strDate = dateFormat.format(date);
        etFilter.setText(strDate);
        Query query = FirebaseDatabase.getInstance().getReference("Transaksi").orderByChild("emailcust").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    classtransaksi ct = ds.getValue(classtransaksi.class);
                    if(ct.getWaktutransaksi().contains(etFilter.getText().toString())){
                        listTrans.add(ct);
                    }
                }
                adapter2 = new historyAdapter(listTrans);
                rvHistory.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
