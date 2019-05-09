package com.singpaulee.blacksandapp.model.siapbuang;

import com.google.gson.annotations.SerializedName;

public class JavaModel {

    @SerializedName("nama")
    private String nama;

    @SerializedName("alamat")
    private String alamat;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

}


