package com.example.parkirin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText eduser, edpass, edconfpass, edname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


    }



    public void klikLogin(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }



    public void klikRegis(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }





}
