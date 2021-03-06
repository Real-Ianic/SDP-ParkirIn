package com.example.sdppetugas;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    TextView tvTempat,tvPlat,tvJenis,tvDurasi,tvTglWaktu,tvKendaraan,tvTotal,tvStatus;
    Button btnTolak,btnCheckin,btnCheckout;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDurasi = itemView.findViewById(R.id.txtHistoryDurasi);
        tvJenis = itemView.findViewById(R.id.txtHistoryJenis);
        tvKendaraan = itemView.findViewById(R.id.txtHistoryKendaraan);
        tvPlat = itemView.findViewById(R.id.txtHistoryPlat);
        tvTempat = itemView.findViewById(R.id.txtHistoryTempat);
        tvTglWaktu = itemView.findViewById(R.id.txtHistoryTanggal);
        tvTotal = itemView.findViewById(R.id.txtHistoryTotal);
        tvStatus = itemView.findViewById(R.id.txtHistoryStatus);
        btnTolak = itemView.findViewById(R.id.btnTolak);
        btnCheckin = itemView.findViewById(R.id.btnCheckin);
        btnCheckout = itemView.findViewById(R.id.btnCheckout);
    }
}
