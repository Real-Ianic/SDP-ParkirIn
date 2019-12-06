package com.example.parkirin;

public class lokasi {
    private String nama,alamat,pemilik,linkgambar;
    Double lat,lng;
    private int slotmobil,slotmotor;

    public lokasi() {
    }

    public lokasi(String nama, String alamat, String pemilik, Double lat, Double lng, int slotmobil, int slotmotor, String linkgambar) {
        this.nama = nama;
        this.alamat = alamat;
        this.pemilik = pemilik;
        this.lat = lat;
        this.lng = lng;
        this.slotmobil = slotmobil;
        this.slotmotor = slotmotor;
        this.linkgambar = linkgambar;
    }

    public String getLinkgambar() {
        return linkgambar;
    }

    public void setLinkgambar(String linkgambar) {
        this.linkgambar = linkgambar;
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
