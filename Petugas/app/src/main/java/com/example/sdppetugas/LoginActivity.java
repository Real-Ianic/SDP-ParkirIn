package com.example.sdppetugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail,etPass;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        b = findViewById(R.id.btnLogin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etEmail.getText().toString().equals("") && !etPass.getText().toString().equals("")){
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    finish();
                    i.putExtra("email",etEmail.getText().toString());
                    startActivity(i);
                }
            }
        });
    }
}
