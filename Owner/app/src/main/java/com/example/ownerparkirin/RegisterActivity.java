package com.example.ownerparkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    DatabaseReference reff;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    EditText ednama, edemail, edpassword, edconfpassword, ednohp;
    Button btnregist;
    String nama, email, pass, confpass;
    int nohp;
    TextView tologin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tologin = findViewById(R.id.tvtologin);
        ednama = findViewById(R.id.ed_regis_name);
        edemail = findViewById(R.id.ed_regis_email);
        edpassword = findViewById(R.id.ed_regis_pass);
        edconfpassword = findViewById(R.id.ed_regis_confpass);
        ednohp = findViewById(R.id.ed_regis_nohp);

        progressBar = new ProgressBar(this);
        database=FirebaseDatabase.getInstance();
        //reff = database.getReference().child("Owner");



        btnregist = findViewById(R.id.btnregister_register);
        btnregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=edpassword.getText().toString();
                confpass=edconfpassword.getText().toString();
                if (!confpass.equals(pass)){
                    Toast.makeText(RegisterActivity.this, "Confirm Password harus sama dengan password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(RegisterActivity.this, "confpass == pass!", Toast.LENGTH_SHORT).show();
                    String namatemp = ednama.getText().toString();
                    String emailtemp= edemail.getText().toString();
                    String pass = edpassword.getText().toString();
                    String hp = ednohp.getText().toString();

                    if(!(namatemp.isEmpty()||emailtemp.isEmpty()||pass.isEmpty()||hp.isEmpty())){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ownerRef = database.getReference("Owner");

                        OwnerClass owner = new OwnerClass();
                        owner.setName(namatemp);
                        owner.setEmail(emailtemp);
                        owner.setPassword(pass);
                        owner.setNohp(hp);

                        ownerRef.push().setValue(owner);
                        Toast.makeText(RegisterActivity.this, "Berhasil Register!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // move to login activity
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


    }
}
