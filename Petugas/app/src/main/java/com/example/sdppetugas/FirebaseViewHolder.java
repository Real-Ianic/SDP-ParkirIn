package com.example.sdppetugas;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView plat,durasi;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        plat = itemView.findViewById(R.id.platnomor);
        durasi = itemView.findViewById(R.id.durasihari);


    }
}
