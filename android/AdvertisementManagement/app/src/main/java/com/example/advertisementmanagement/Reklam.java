package com.example.advertisementmanagement;

public class Reklam {

    private String firmaID;
    private String firmaAdi;
    private String latitude;
    private String longitude;
    private String kampanyaIcerik;
    private String kampanyaSuresi;
    private String kategori;

    public Reklam() {

    }

    public Reklam(String firmaID, String firmaAdi, String latitude, String longitude, String kampanyaIcerik, String kampanyaSuresi, String kategori) {
        this.firmaID = firmaID;
        this.firmaAdi = firmaAdi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kampanyaIcerik = kampanyaIcerik;
        this.kampanyaSuresi = kampanyaSuresi;
        this.kategori = kategori;
    }

    public Reklam(String firmaAdi, String latitude, String longitude, String kampanyaIcerik, String kampanyaSuresi, String kategori) {
        this.firmaAdi = firmaAdi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kampanyaIcerik = kampanyaIcerik;
        this.kampanyaSuresi = kampanyaSuresi;
        this.kategori = kategori;
    }

    public String getFirmaID() {
        return firmaID;
    }

    public void setFirmaID(String firmaID) {
        this.firmaID = firmaID;
    }

    public String getFirmaAdi() {
        return firmaAdi;
    }

    public void setFirmaAdi(String firmaAdi) {
        this.firmaAdi = firmaAdi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKampanyaIcerik() {
        return kampanyaIcerik;
    }

    public void setKampanyaIcerik(String kampanyaIcerik) {
        this.kampanyaIcerik = kampanyaIcerik;
    }

    public String getKampanyaSuresi() {
        return kampanyaSuresi;
    }

    public void setKampanyaSuresi(String kampanyaSuresi) {
        this.kampanyaSuresi = kampanyaSuresi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
