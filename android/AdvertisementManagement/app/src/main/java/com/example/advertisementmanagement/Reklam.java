package com.example.advertisementmanagement;

public class Reklam {

    private String firmaID;
    private String firmaAdi;
    private String lokasyon;
    private String kampanyaIcerik;
    private String kampanyaSuresi;
    private String kategori;

    public Reklam() {

    }

    public Reklam(String firmaID, String firmaAdi, String lokasyon, String kampanyaIcerik, String kampanyaSuresi, String kategori) {
        this.firmaID = firmaID;
        this.firmaAdi = firmaAdi;
        this.lokasyon = lokasyon;
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

    public String getLokasyon() {
        return lokasyon;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
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
        this.kampanyaSuresi = kategori;
    }
}
