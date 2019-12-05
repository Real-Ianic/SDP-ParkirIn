package com.example.adminfix;

public class OperatorClass {
    private String nama;
    private String email;
    private String password;
    private String nohp;
    private String idLokasi;

    public OperatorClass()
    {

    }

    public OperatorClass(String nama, String email, String password, String nohp, String idLokasi)
    {
        this.setNama(nama);
        this.setEmail(email);
        this.setPassword(password);
        this.setNohp(nohp);
        this.setIdLokasi(idLokasi);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(String idLokasi) {
        this.idLokasi = idLokasi;
    }
}
