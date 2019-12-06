package com.example.parkirin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class profilefragment extends Fragment {
    Button edit,histori,pass,hub;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            getActivity().finish();
        }
        return true;
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
                i.putExtra("email",((MainActivity)getActivity()).email);
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
