package com.example.ownerparkirin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {
    ArrayList<classtransaksi> listTransaksi;

    public TransaksiAdapter(ArrayList<classtransaksi> listTransaksi)
    {
        this.listTransaksi = listTransaksi;
    }

    @NonNull
    @Override
    public TransaksiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaksi_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdapter.ViewHolder holder, int position) {
        classtransaksi tempTrans = listTransaksi.get(position);

        holder.tvDurasi.setText(tempTrans.getDurasijam());
        holder.tvEmailcust.setText(tempTrans.getEmailcust());
        holder.tvPlat.setText(tempTrans.getPlatnomor());
        holder.tvJenis.setText(tempTrans.getJenis());
        holder.tvStatus.setText(tempTrans.getStatus());
        holder.tvTempat.setText(tempTrans.getTempat());
        holder.tvTipe.setText(tempTrans.getTipekendaraan());
        holder.tvTotal.setText(tempTrans.getTotal());
        holder.tvWaktu.setText(tempTrans.getWaktutransaksi());
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDurasi,tvEmailcust,tvJenis,tvPlat,tvStatus,tvTempat,tvTipe,tvTotal,tvWaktu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDurasi = itemView.findViewById(R.id.durasi);
            tvEmailcust = itemView.findViewById(R.id.emailCust);
            tvPlat = itemView.findViewById(R.id.platnomor);
            tvJenis = itemView.findViewById(R.id.jenis);
            tvStatus = itemView.findViewById(R.id.status);
            tvTempat = itemView.findViewById(R.id.tempat);
            tvTipe = itemView.findViewById(R.id.tipekendaraan);
            tvTotal = itemView.findViewById(R.id.total);
            tvWaktu = itemView.findViewById(R.id.waktutransaksi);
        }
    }
}
