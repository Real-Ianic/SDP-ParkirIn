package com.example.sdppetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvKendaraan;
    ArrayList<String> arrInfo;
    ArrayList<DataSetFire> arrayList;
    ArrayList<String> arrOnGoing,arrPlat;
    ArrayAdapter<String> infoAdapter;
    EditText etSearch;
    TextWatcher twSearch;
    ConstraintLayout mConstraintLayout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Firebase mRef;
    FirebaseRecyclerOptions<DataSetFire> options;
    FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transaksi");
        rvKendaraan = findViewById(R.id.recyclerview);
        rvKendaraan.setHasFixedSize(true);
        rvKendaraan.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<DataSetFire>();
        mDatabase.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(mDatabase, DataSetFire.class).build();
        adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, int i, @NonNull final DataSetFire dataSetFire) {

                firebaseViewHolder.plat.setText(dataSetFire.getPlatnomor());
                firebaseViewHolder.durasi.setText(dataSetFire.getDurasiutktampil());
                firebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, statusKendaraan.class);
                        i.putExtra("plat",dataSetFire.getPlatnomor());
                        i.putExtra("durasi",dataSetFire.getDurasiutktampil());
                        i.putExtra("total",dataSetFire.getTotal());
                        i.putExtra("status",dataSetFire.getStatus());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.row,parent,false));

            }
        };

        rvKendaraan.setAdapter(adapter);

        mRef = new Firebase("https://parkirin-160fb.firebaseio.com/transaksi");
        etSearch = findViewById(R.id.etSearch);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.cl);
        arrInfo =  new ArrayList<String>();
        arrOnGoing = new ArrayList<String>();
        arrPlat = new ArrayList<String>();
        arrInfo.add("dicky");
        infoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrInfo);

        if(getIntent()!=null){
            String plat = getIntent().getStringExtra("plat");
            for (int a = 0; a < arrInfo.size(); a++) {
                if(arrInfo.get(a).equals(getIntent().getStringExtra("nama"))){
                    arrOnGoing.add(arrInfo.get(a));
                    arrPlat.add(plat);
                    arrInfo.remove(a);
                }
            }
            infoAdapter.notifyDataSetChanged();
        }
        twSearch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                infoAdapter.getFilter().filter(charSequence);
                infoAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etSearch.addTextChangedListener(twSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.optLogout){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        }
        return true;
    }
}
