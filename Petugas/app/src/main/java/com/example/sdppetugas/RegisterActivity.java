package com.example.sdppetugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView tvtoLogin;
    EditText ednama,edemail,edpass,edconfpass;
    Button btnregister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvtoLogin = findViewById(R.id.tvtoLogin);
        edemail = findViewById(R.id.edemail_register);
        ednama = findViewById(R.id.ednama_register);
        edpass = findViewById(R.id.edpassword_register);
        edconfpass = findViewById(R.id.edconfpass_register);
        tvtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnregister = findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


}
