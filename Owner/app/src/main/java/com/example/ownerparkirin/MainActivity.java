package com.example.ownerparkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<LokasiClass> listLokasi = new ArrayList<>();
    List<classtransaksi> listTransaksi = new ArrayList<>();
    List<OwnerClass> listOwner = new ArrayList<>();
    List<String> listId = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    static OwnerClass currentOwner;

    TaskCompletionSource<List<OwnerClass>> dbSource = new TaskCompletionSource<>();
    Task dbTask = dbSource.getTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        currentOwner = getIntent.getParcelableExtra("loggedOwner");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        changeFragment(new LocationFragment(), "");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.mylocationsmenu){
                            changeFragment(new LocationFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.transactionsmenu){
                            changeFragment(new TransactionsFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.myprofilemenu){
                            changeFragment(new ProfileFragment(), "");
                        }
                        else if(menuItem.getItemId() == R.id.reportmenu){
                            changeFragment(new ReportFragment(), "");
                        }
                        return true;
                    }
                }
        );

    }



    public void changeFragment(Fragment f, String data) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        f.setArguments(bundle);
        ft.replace(R.id.container, f);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutop,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutmenu){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    //Custom Functions

    public void addLocation(LokasiClass lokasi)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lokasiRef = database.getReference("Lokasi");

        lokasiRef.push().setValue(lokasi);
    }

    public void editOwner(final OwnerClass owner)
    {
        dbSource = new TaskCompletionSource<>();
        dbTask = dbSource.getTask();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Owner");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> templistOwner = (HashMap<String, Object>)dataSnapshot.getValue();
                listOwner.clear();
                listId.clear();
                for (String key : templistOwner.keySet())
                {
                    HashMap<String,Object> tempOwner = (HashMap<String,Object>)templistOwner.get(key);
                    String tempEmail = String.valueOf(tempOwner.get("email"));
                    String tempPassword = String.valueOf(tempOwner.get("password"));
                    String tempNama = String.valueOf(tempOwner.get("name"));
                    String tempNohp = String.valueOf(tempOwner.get("nohp"));

                    OwnerClass owner = new OwnerClass();
                    owner.setEmail(tempEmail);
                    owner.setPassword(tempPassword);
                    owner.setName(tempNama);
                    owner.setNohp(tempNohp);

                    listOwner.add(owner);
                    listId.add(key);
                }
                dbSource.setResult(listOwner);
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
                String id = "";
                for(int i=0;i<listOwner.size();i++)
                {
                    if(listOwner.get(i).getEmail().equals(owner.getEmail()))
                    {
                        id = listId.get(i);
                    }
                }

                DatabaseReference reff;
                reff = db.getReference("Owner/"+id);

                reff.setValue(owner);
            }
        });

    }

    public void getLokasi()
    {
        listLokasi.clear();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Lokasi");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();

                for (String key : tempLokasi.keySet())
                {
                    HashMap<String,Object> lokasi = (HashMap<String,Object>) tempLokasi.get(key);
                    LokasiClass locc = new LokasiClass();
                    locc.setPemilik(lokasi.get("pemilik").toString());
                    locc.setNama(lokasi.get("nama").toString());

                    listLokasi.add(locc);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        for(int i=0;i<listLokasi.size();i++)
        {
            if(!listLokasi.get(i).getPemilik().equals(currentOwner.getName()))
            {
                listLokasi.set(i,null);
            }
        }

        for(int i=0;i<listLokasi.size();i++)
        {
            if(listLokasi.get(i).equals(null))
            {
                listLokasi.remove(i);
                i--;
            }
        }
    }
}
