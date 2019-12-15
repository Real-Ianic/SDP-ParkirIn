package com.example.adminfix;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditLokasiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditLokasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditLokasiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText nama,alamat,pemilik,slotmotor,slotmobil,lat,lng;
    String sNama,sAlamat,sPemilik,temptempat;
    double sLat,sLng;
    private Uri filePath;
    private ImageView imageView;
    int sMotor,sMobil;
    String key,link;
    DatabaseReference reff,reff2;
    Spinner spinner;
    Button simpan,btnimage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ArrayList<String> arrLokasi = new ArrayList<String>();
    private ProgressBar progressBar;
    LokasiClass l;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditLokasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditLokasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditLokasiFragment newInstance(String param1, String param2) {
        EditLokasiFragment fragment = new EditLokasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_lokasi, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

     */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nama=view.findViewById(R.id.etNamaLokasiEdit);
        simpan=view.findViewById(R.id.btnEditOwner);
        alamat=view.findViewById(R.id.etAlamatLokasiEdit);
        slotmobil=view.findViewById(R.id.etSlotmobilEdit);
        slotmotor=view.findViewById(R.id.etEditOwnerNohp);
        pemilik=view.findViewById(R.id.etPemilikEdit);
        lat=view.findViewById(R.id.etLatitudeEdit);
        spinner=view.findViewById(R.id.spinnerEditOwner);
        progressBar=view.findViewById(R.id.progressBarEdit);
        lng=view.findViewById(R.id.etLongitudeEdit);
        btnimage=view.findViewById(R.id.btnGambarEdit);
        imageView=view.findViewById(R.id.previewImageEdit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("gambar");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("Lokasi");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()) {
                    key = ds.getKey();
                    reff = FirebaseDatabase.getInstance().getReference().child("Lokasi").child(key);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            arrLokasi.add(ds.child("nama").getValue().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arrLokasi);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    temptempat = spinner.getSelectedItem().toString();
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    Query query1 = rootRef.child("Lokasi").orderByChild("nama").equalTo(temptempat);
                                    ValueEventListener valueEventListener1 = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                key = ds.getKey();
                                                reff2 = FirebaseDatabase.getInstance().getReference().child("Lokasi").child(key);
                                                reff2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        nama.setText(temptempat);
                                                        alamat.setText(dataSnapshot.child("alamat").getValue().toString());
                                                        lat.setText(dataSnapshot.child("lat").getValue().toString());
                                                        lng.setText(dataSnapshot.child("lng").getValue().toString());
                                                        slotmobil.setText(dataSnapshot.child("slotmobil").getValue().toString());
                                                        slotmotor.setText(dataSnapshot.child("slotmotor").getValue().toString());
                                                        pemilik.setText(dataSnapshot.child("pemilik").getValue().toString());
                                                        link = dataSnapshot.child("linkgambar").getValue().toString();
                                                        Picasso.get().load(link).into(imageView);
                                                        simpan.setOnClickListener(new View.OnClickListener() {
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
                                                                                        reff2.setValue(l);
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
                                                                    Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
                                                                    nama.setText("");
                                                                    alamat.setText("");
                                                                    pemilik.setText("");
                                                                    slotmotor.setText("");
                                                                    slotmobil.setText("");
                                                                    lat.setText("");
                                                                    lng.setText("");
                                                                    imageView.setImageResource(0);
                                                                }else{
                                                                    Toast.makeText(getContext(), "Semua Data Wajib Diisi", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
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
                                    query1.addListenerForSingleValueEvent(valueEventListener1);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
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
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            Picasso.get().load(filePath).into(imageView);
        }
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

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
