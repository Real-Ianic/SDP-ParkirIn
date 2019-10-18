package com.example.parkirin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    Button edit,histori,pass,hub;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit=view.findViewById(R.id.btnubah);
        histori=view.findViewById(R.id.btnPengaturan);
        pass=view.findViewById(R.id.btnHistori);
        hub=view.findViewById(R.id.btnprofil);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(i);
            }
        });
        histori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),historyActivity.class);
                startActivity(i);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditPassActivity.class);
                startActivity(i);
            }
        });
        hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="08123252323";
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                startActivity(i);
            }
        });
    }
}
