package com.example.adminfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ArrayList<classtransaksi> listTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNav);
        changeFragment(new LocationFragment(), "");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menuLocation){
                            changeFragment(new LocationFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuUser){
                            changeFragment(new UserFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuOperator){
                            changeFragment(new OperatorFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuOwner){
                            changeFragment(new OwnerFragment(), "");
                        }
                        else if (menuItem.getItemId() == R.id.menuTransaction){
                            changeFragment(new TransactionFragment(), "");
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    //Functional Functions (Add Edit Delete etc)
    // Operator
    public void addOperator(OperatorClass op)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petugasRef = database.getReference("Petugas");

        petugasRef.push().setValue(op);

    }

    public void editOperator(String id, OperatorClass op)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petugasRef = database.getReference("Petugas/" + id);

        petugasRef.setValue(op);
    }

    public void deleteOperator(String id)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petugasRef = database.getReference("Petugas/" + id);

        petugasRef.removeValue();
    }

    // User
    public void addUser(Member user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Member");

        userRef.push().setValue(user);
    }

    public void editUser(String id, Member user)
    {

    }

    public void deleteUser(String id)
    {

    }

    // Owner
    public void addOwner(OwnerClass owner)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ownerRef = database.getReference("Owner");

        ownerRef.push().setValue(owner);
    }

    public void editOwner(String id, OwnerClass owner)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ownerRef = database.getReference("Owner/" +id);

        ownerRef.setValue(owner);
    }

    public void deleteOwner(String id)
    {

    }

    public List<classtransaksi> getTransactions()
    {
        listTransaksi.clear();


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Transaksi");

        ref = db.getReference("Transaksi");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> tempLokasi = (HashMap<String, Object>)dataSnapshot.getValue();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return listTransaksi;
    }
}
