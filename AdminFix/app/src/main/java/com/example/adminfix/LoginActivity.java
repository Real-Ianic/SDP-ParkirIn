package com.example.adminfix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edusername, edpassword;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edpassword = findViewById(R.id.edpasswordadmin);
        edusername = findViewById(R.id.edusernameadmin);
        btnlogin = findViewById(R.id.btnloginadmin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = edusername.getText().toString();
                String passw = edpassword.getText().toString();
                if (uname.equals("admin") && passw.equals("admin")){
                    Toast.makeText(LoginActivity.this, "Welcome back Admin!♡", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "you are not admin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void tomain(){
    }
}
