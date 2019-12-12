package com.example.ownerparkirin;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin, btntoregist;
    EditText edemail, edpass;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    TextView toregist;

    List<OwnerClass> listOwner = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edemail = findViewById(R.id.ed_login_email);
        edpass = findViewById(R.id.ed_login_password);
        btnlogin = findViewById(R.id.btnloginowner);
        toregist = findViewById(R.id.tvtoregist);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login())
                {
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    int tempInt = 0;
                    String email = edemail.getText().toString();
                    String password = edpass.getText().toString();
                    for(int i=0;i<listOwner.size();i++)
                    {
                        if(listOwner.get(i).getEmail().equals(email) && listOwner.get(i).getPassword().equals(password))
                        {
                            tempInt = i;
                        }
                    }
                    in.putExtra("loggedOwner",listOwner.get(tempInt));
                    startActivity(in);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Gagal Login !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean login()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Owner");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> templistOwner = (HashMap<String, Object>)dataSnapshot.getValue();
                listOwner.clear();
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        String email = edemail.getText().toString();
        String password = edpass.getText().toString();

        for(int i=0;i<listOwner.size();i++)
        {
            if(listOwner.get(i).getEmail().equals(email) && listOwner.get(i).getPassword().equals(password))
            {
                return true;
            }
        }
        return false;
    }
}
