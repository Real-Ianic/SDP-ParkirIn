package com.example.adminfix;

public class classtransaksi {
    String tempat,jenis,platnomor,tipekendaraan,emailcust,waktutransaksi,durasiutktampil,status;
    int durasijam,total;

    public classtransaksi(){

    }

    public classtransaksi(String tempat, String jenis, String platnomor, String tipekendaraan, int durasijam, int total, String emailcust,String waktutransaksi,String durasiutktampil,String status) {
        this.tempat = tempat;
        this.jenis = jenis;
        this.platnomor = platnomor;
        this.tipekendaraan = tipekendaraan;
        this.durasijam = durasijam;
        this.total = total;
        this.emailcust=emailcust;
        this.waktutransaksi=waktutransaksi;
        this.durasiutktampil=durasiutktampil;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDurasiutktampil() {
        return durasiutktampil;
    }

    public void setDurasiutktampil(String durasiutktampil) {
        this.durasiutktampil = durasiutktampil;
    }

    public String getWaktutransaksi() {
        return waktutransaksi;
    }

    public void setWaktutransaksi(String waktutransaksi) {
        this.waktutransaksi = waktutransaksi;
    }

    public int getDurasijam() {
        return durasijam;
    }

    public void setDurasijam(int durasijam) {
        this.durasijam = durasijam;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getPlatnomor() {
        return platnomor;
    }

    public void setPlatnomor(String platnomor) {
        this.platnomor = platnomor;
    }

    public String getTipekendaraan() {
        return tipekendaraan;
    }

    public void setTipekendaraan(String tipekendaraan) {
        this.tipekendaraan = tipekendaraan;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getEmailcust() {
        return emailcust;
    }

    public void setEmailcust(String emailcust) {
        this.emailcust = emailcust;
    }
}
