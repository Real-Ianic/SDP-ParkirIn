package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText etNama,etAlamat;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Ubah Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etAlamat = findViewById(R.id.etAlamatEdit);
        etNama = findViewById(R.id.txtNamaEdit);
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etNama.setText(dataSnapshot.child("nama").getValue().toString());
                etAlamat.setText(dataSnapshot.child("alamat").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View view) {
        reff.child("nama").setValue(etNama.getText().toString());
        reff.child("alamat").setValue(etAlamat.getText().toString());
        Toast.makeText(this, "Profil Berhasil Di Ubah", Toast.LENGTH_SHORT).show();
        finish();
    }
}
