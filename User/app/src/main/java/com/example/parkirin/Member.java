package com.example.parkirin;

public class Member {
    private String nama,email,alamat,nohp;
    private int Saldo;

    public Member() {
    }

    public Member(String nama, String email,String alamat, String nohp, int Saldo) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.nohp = nohp;
        this.Saldo = Saldo;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
