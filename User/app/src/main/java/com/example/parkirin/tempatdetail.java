package com.example.parkirin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;

public class tempatdetail extends AppCompatActivity {

    DatabaseReference reff;
    String namatempat,email,link;
    TextView tvSlotmotor,tvSlotmobil,tvAlamat,tvNama,tvJarak;
    ImageView img;
    Button pesan;
    Query query;
    Double jarak;
    String key;
    String currlat,currlng;
    Location currLoc,desLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempatdetail);
        img=findViewById(R.id.gambartempatdetail);
        tvSlotmobil=findViewById(R.id.slotroda4detail);
        tvSlotmotor=findViewById(R.id.slotroda2detail);
        pesan=findViewById(R.id.btnBuatpesanan);
        tvNama=findViewById(R.id.namatempatdetail);
        tvJarak=findViewById(R.id.jarakdetail);
        tvAlamat=findViewById(R.id.alamattempatdetail);
        namatempat=getIntent().getStringExtra("tempat");
        currlat = getIntent().getStringExtra("currlat");
        currlng = getIntent().getStringExtra("currlng");
        currLoc = new Location("Lokasi Sekarang");
        currLoc.setLongitude(Double.parseDouble(currlng));
        currLoc.setLatitude(Double.parseDouble(currlat));
        email=getIntent().getStringExtra("email");
        tvNama.setText(namatempat);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("Lokasi").orderByChild("nama").equalTo(namatempat);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("Lokasi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            link=dataSnapshot.child("linkgambar").getValue(String.class);
                            tvAlamat.setText(dataSnapshot.child("alamat").getValue().toString());
                            tvSlotmobil.setText("Sisa Slot Mobil : "+dataSnapshot.child("slotmobil").getValue()+"");
                            tvSlotmotor.setText("Sisa Slot Motor : "+dataSnapshot.child("slotmotor").getValue()+"");
                            desLoc = new Location("Tujuan");
                            NumberFormat f = NumberFormat.getInstance();
                            try {
                                desLoc.setLatitude(f.parse(dataSnapshot.child("lat").getValue().toString()).doubleValue());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                desLoc.setLongitude(f.parse(dataSnapshot.child("lng").getValue().toString()).doubleValue());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            jarak = (double) (Math.round(currLoc.distanceTo(desLoc) / 1000 * 100)) / 100;
                            tvJarak.setText(jarak+" KM");
                            Picasso.get().load(link).into(img);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(tempatdetail.this,pemesananActivity.class);
                i.putExtra("tempat",namatempat);
                i.putExtra("email",email);
                startActivity(i);
                finish();
            }
        });
    }
}
