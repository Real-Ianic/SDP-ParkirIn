package com.example.parkirin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ListViewHolder> {
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerhistory,parent,false);
        ListViewHolder holder = new ListViewHolder(view);
        return holder;
    }
    ArrayList<classtransaksi> ct;
    public historyAdapter(ArrayList<classtransaksi> ct){
        this.ct=ct;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.tvDurasi.setText("Durasi : "+ ct.get(position).getDurasiutktampil());
        holder.tvJenis.setText("Jenis : "+ct.get(position).getJenis());
        holder.tvKendaraan.setText("Jenis Kendaraan : "+ct.get(position).getTipekendaraan());
        holder.tvPlat.setText("Plat Nomor : "+ct.get(position).getPlatnomor());
        holder.tvTempat.setText("Tempat : "+ct.get(position).getTempat());
        holder.tvTglWaktu.setText("Tanggal & Waktu : "+ct.get(position).getWaktutransaksi());
        holder.tvTotal.setText("Total : "+ct.get(position).getTotal()+"");
        if(ct.get(position).getStatus().equals("Selesai")){
            holder.tvStatus.setTextColor(Color.GREEN);
        }
        if(ct.get(position).getStatus().equals("Ditolak")){
            holder.tvStatus.setTextColor(Color.RED);
        }
        holder.tvStatus.setText(ct.get(position).getStatus()+"");
    }

    @Override
    public int getItemCount() {
        return ct.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTempat,tvPlat,tvJenis,tvDurasi,tvTglWaktu,tvKendaraan,tvTotal,tvStatus;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDurasi = itemView.findViewById(R.id.txtHistoryDurasi);
            tvJenis = itemView.findViewById(R.id.txtHistoryJenis);
            tvKendaraan = itemView.findViewById(R.id.txtHistoryKendaraan);
            tvPlat = itemView.findViewById(R.id.txtHistoryPlat);
            tvTempat = itemView.findViewById(R.id.txtHistoryTempat);
            tvTglWaktu = itemView.findViewById(R.id.txtHistoryTanggal);
            tvTotal = itemView.findViewById(R.id.txtHistoryTotal);
            tvStatus = itemView.findViewById(R.id.txtHistoryStatus);
        }
    }
}
