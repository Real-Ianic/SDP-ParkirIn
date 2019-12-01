package com.example.adminfix;

public class LokasiClass {
    private String nama,alamat,pemilik;
    Double lat,lng;
    private int slotmobil,slotmotor;

    public LokasiClass() {
    }

    public LokasiClass(String nama, String alamat, String pemilik, Double lat, Double lng, int slotmobil, int slotmotor) {
        this.nama = nama;
        this.alamat = alamat;
        this.pemilik = pemilik;
        this.lat = lat;
        this.lng = lng;
        this.slotmobil = slotmobil;
        this.slotmotor = slotmotor;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getSlotmobil() {
        return slotmobil;
    }

    public void setSlotmobil(int slotmobil) {
        this.slotmobil = slotmobil;
    }

    public int getSlotmotor() {
        return slotmotor;
    }

    public void setSlotmotor(int slotmotor) {
        this.slotmotor = slotmotor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

}
