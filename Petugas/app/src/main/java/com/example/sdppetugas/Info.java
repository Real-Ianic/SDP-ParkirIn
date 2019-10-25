package com.example.sdppetugas;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {
    private String nama,tipeOrder,tipeKendaraan,timeOrder;

    public Info(String nama, String tipeOrder, String tipeKendaraan, String timeOrder) {
        this.nama = nama;
        this.tipeOrder = tipeOrder;
        this.tipeKendaraan = tipeKendaraan;
        this.timeOrder = timeOrder;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTipeOrder() {
        return tipeOrder;
    }

    public void setTipeOrder(String tipeOrder) {
        this.tipeOrder = tipeOrder;
    }

    public String getTipeKendaraan() {
        return tipeKendaraan;
    }

    public void setTipeKendaraan(String tipeKendaraan) {
        this.tipeKendaraan = tipeKendaraan;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }

    protected Info(Parcel in) {
        nama = in.readString();
        tipeOrder = in.readString();
        tipeKendaraan = in.readString();
        timeOrder = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nama);
        parcel.writeString(tipeOrder);
        parcel.writeString(tipeKendaraan);
        parcel.writeString(timeOrder);
    }
}
