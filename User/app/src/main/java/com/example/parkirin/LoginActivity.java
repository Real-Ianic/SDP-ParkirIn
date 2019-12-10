package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView SignUp;
    String emaillogin,pass;
    FusedLocationProviderClient fusedLocationProviderClient;
    Query reff;
    FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LoginActivity.this);
        fusedLocationProviderClient.getLastLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.input_email);
        password=findViewById(R.id.input_password);
        login=findViewById(R.id.btn_login);
        SignUp=findViewById(R.id.link_signup);
        statusCheck();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation();
        if(user != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent regis = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(regis);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emaillogin=email.getText().toString();
                pass=password.getText().toString();
                if(emaillogin.isEmpty()){
                    email.setError("Email Harus Diisi!");
                }
                if(pass.isEmpty()){
                    password.setError("Password Harus Diisi!");
                }
                if(!emaillogin.isEmpty()&&!pass.isEmpty()) {
                    reff = FirebaseDatabase.getInstance().getReference().child("Member").orderByChild("email").equalTo(emaillogin);
                    reff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                mAuth.signInWithEmailAndPassword(emaillogin, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LoginActivity.this);
                                            fusedLocationProviderClient.getLastLocation();
                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            i.putExtra("email",email.getText().toString()+"");
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Login gagal!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(LoginActivity.this, "Login gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Semua Field Harus Terisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aplikasi membutuhkan akses lokasi apakah anda ingin menyalakan lokasi?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
