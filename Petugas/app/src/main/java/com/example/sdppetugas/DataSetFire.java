package com.example.sdppetugas;

public class DataSetFire {
    String platnomor;
    String durasiutktampil;
    int total;
    String status;
    public DataSetFire() {
    }

    public DataSetFire(String platnomor, String durasiutktampil, String status) {
        this.platnomor = platnomor;
        this.durasiutktampil = durasiutktampil;
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatnomor() {
        return platnomor;
    }

    public void setPlatnomor(String platnomor) {
        this.platnomor = platnomor;
    }

    public String getDurasiutktampil() {
        return durasiutktampil;
    }

    public void setDurasiutktampil(String durasiutktampil) {
        this.durasiutktampil = durasiutktampil;
    }
}
