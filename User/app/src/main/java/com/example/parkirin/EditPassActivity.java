package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditPassActivity extends AppCompatActivity {

    EditText etOldpass,etNewpass,etNewpassconf;
    String oldPass,newPass,newPassconf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        getSupportActionBar().setTitle("Ganti Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etOldpass = findViewById(R.id.etPassLama);
        etNewpass = findViewById(R.id.etPassbaru);
        etNewpassconf = findViewById(R.id.etPassbaruconf);
    }

    public void onClick(View view) {
        oldPass = etOldpass.getText().toString();
        newPass = etNewpass.getText().toString();
        newPassconf = etNewpassconf.getText().toString();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),oldPass);
        if(newPass.equals(newPassconf)){
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditPassActivity.this, "Password Berhasil Diganti", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(EditPassActivity.this, "Password Gagal Diganti", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Konfirmasi Password Harus Sama Dengan Password", Toast.LENGTH_SHORT).show();
        }
    }
}
