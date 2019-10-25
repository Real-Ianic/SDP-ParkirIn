package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView SignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.input_email);
        password=findViewById(R.id.input_password);
        login=findViewById(R.id.btn_login);
        SignUp=findViewById(R.id.link_signup);
        mAuth=FirebaseAuth.getInstance();
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
                String emaillogin=email.getText().toString();
                String pass=password.getText().toString();
                if(emaillogin.isEmpty()){
                    email.setError("Email Harus Diisi!");
                }
                if(pass.isEmpty()){
                    password.setError("Password Harus Diisi!");
                }
                if(!emaillogin.isEmpty()&&!pass.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(emaillogin, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Semua Field Harus Terisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
