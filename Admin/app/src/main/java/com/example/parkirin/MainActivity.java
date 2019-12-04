package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText eduser, edpass;
    ArrayList<Users> users;
    ArrayList<Worker> workers;
    boolean cek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eduser = findViewById(R.id.edtUsername);
        edpass = findViewById(R.id.edtPassword);

        users = new ArrayList<Users>();
        workers = new ArrayList<Worker>();
        cek= false;

    }

    public void klikLogin(View v) {
        Toast.makeText(this, "HALO", Toast.LENGTH_SHORT).show();
        if (eduser.getText().toString().equals("admin") && edpass.getText().toString().equals("admin")) {
            Intent i = new Intent(this, AdminHome.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Username / Password salah !", Toast.LENGTH_SHORT).show();
        }
        edpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        cek=false;
    }

    public void klikRegis(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void viewpass(View v){
        if(!cek) {
            edpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            cek=true;
        }
        else{
            edpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            cek=false;
        }
    }
}
