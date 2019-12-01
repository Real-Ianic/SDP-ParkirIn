package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText nama,email,password,alamat,nohp,passconf;
    TextView linkLogin;
    Member member;
    Button register;
    DatabaseReference reff;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkLogin = findViewById(R.id.link_login);
        nama=findViewById(R.id.regisname);
        email=findViewById(R.id.regisemail);
        password=findViewById(R.id.regispass);
        alamat=findViewById(R.id.regisalamat);
        nohp=findViewById(R.id.regisnohp);
        passconf=findViewById(R.id.regispassconf);
        register=findViewById(R.id.btn_signup);
        progressBar = new ProgressBar(this);
        database=FirebaseDatabase.getInstance();
        reff = database.getReference("Member");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namatemp=nama.getText().toString();
                final String emailtemp=email.getText().toString();
                String pass = password.getText().toString();
                final String almt = alamat.getText().toString();
                final String hp = nohp.getText().toString();
                if(namatemp.isEmpty()){
                    nama.setError( "Nama harus diisi!" );
                }
                if(emailtemp.isEmpty()){
                    email.setError( "Email harus diisi!" );
                }
                if(pass.isEmpty()){
                    password.setError( "Password harus diisi!" );
                }
                if(passconf.getText().toString().isEmpty()){
                    passconf.setError( "Konfirmasi Password harus diisi!" );
                }
                if(!passconf.getText().toString().equals(pass)){
                    password.setError( "Konfirmasi Password Harus Sama Dengan Password" );
                }
                if(almt.isEmpty()){
                    alamat.setError( "Alamat harus diisi!" );
                }
                if(hp.isEmpty()){
                    nohp.setError( "Nomor HP harus diisi!" );
                }
                progressBar.setVisibility(View.VISIBLE);
                if(!(namatemp.isEmpty()||emailtemp.isEmpty()||pass.isEmpty()||almt.isEmpty()||hp.isEmpty())&&pass.equals(passconf.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(emailtemp,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                member = new Member(namatemp,emailtemp,almt,hp,0);
                                FirebaseDatabase.getInstance().getReference("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                    finish();
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
