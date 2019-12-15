package com.example.ownerparkirin;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddLocationFragment extends Fragment {
    EditText nama,alamat,pemilik,slotmotor,slotmobil,lat,lng;
    String sNama,sAlamat,sPemilik;
    double sLat,sLng;
    private Uri filePath;
    private ImageView imageView;
    int sMotor,sMobil;
    DatabaseReference reff;
    Button add,btnimage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    LokasiClass l;

    public AddLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nama=view.findViewById(R.id.etNamaLokasi);
        add=view.findViewById(R.id.btnTambahLokasi);
        alamat=view.findViewById(R.id.etAlamatLokasi);
        slotmobil=view.findViewById(R.id.etSlotmobil);
        slotmotor=view.findViewById(R.id.etSlotmotor);
        pemilik=view.findViewById(R.id.etPemilik);
        lat=view.findViewById(R.id.etLatitude);
        progressBar=view.findViewById(R.id.progressBar);
        lng=view.findViewById(R.id.etLongitude);
        btnimage=view.findViewById(R.id.btnGambar);
        imageView=view.findViewById(R.id.previewImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("gambar");
        reff= FirebaseDatabase.getInstance().getReference();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nama.getText().toString())){
                    nama.setError("Nama Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(alamat.getText().toString())){
                    alamat.setError("Alamat Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(slotmobil.getText().toString())){
                    slotmobil.setError("Slot Mobil Harus Diisi");
                }
                if(TextUtils.isEmpty(slotmotor.getText().toString())){
                    slotmotor.setError("Slot Motor Harus Diisi");
                }
                if(TextUtils.isEmpty(pemilik.getText().toString())){
                    pemilik.setError("Pemilik Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(lat.getText().toString())){
                    lat.setError("Latitude Lokasi Harus Diisi");
                }
                if(TextUtils.isEmpty(lng.getText().toString())){
                    lng.setError("Longitude Lokasi Harus Diisi");
                }
                if(!(nama.getText().toString().isEmpty()||alamat.getText().toString().isEmpty()||slotmotor.getText().toString().isEmpty()||pemilik.getText().toString().isEmpty()||slotmobil.getText().toString().isEmpty()||lat.getText().toString().isEmpty()||lng.getText().toString().isEmpty())){
                    sNama=nama.getText().toString();
                    sAlamat=alamat.getText().toString();
                    sPemilik=pemilik.getText().toString();
                    sMobil=Integer.parseInt(slotmobil.getText().toString());
                    sMotor=Integer.parseInt(slotmotor.getText().toString());
                    sLat=Double.parseDouble(lat.getText().toString());
                    sLng=Double.parseDouble(lng.getText().toString());
                    Query query = reff.child("Lokasi").orderByChild("nama").equalTo(sNama);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getActivity(), "Lokasi Sudah Ada", Toast.LENGTH_SHORT).show();
                            }else{
                                uploadImage();
                                Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                                nama.setText("");
                                alamat.setText("");
                                pemilik.setText("");
                                slotmotor.setText("");
                                slotmobil.setText("");
                                lat.setText("");
                                lng.setText("");
                                imageView.setImageResource(0);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }else{
                    Toast.makeText(getContext(), "Semua Data Wajib Diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    public void chooseImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Pilih Gambar"),1);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadImage(){
        if(filePath!=null){
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(filePath));
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },5000);
                            l = new LokasiClass(sNama,sAlamat,sPemilik,sLat,sLng,sMobil,sMotor,uri.toString());
                            reff.child("Lokasi").push().setValue(l);
                            Toast.makeText(getContext(), "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }else{
            Toast.makeText(getContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            Picasso.get().load(filePath).into(imageView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

}
