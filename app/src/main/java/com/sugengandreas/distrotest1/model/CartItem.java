package com.sugengandreas.distrotest1.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int id_barang;
    private String nama;
    private int harga;
    private String size;
    private int jumlah;
    private int id_pemesanan;

    public CartItem(int id_barang, String nama, int harga, String size, int jumlah) {
        this.id_barang = id_barang;
        this.nama = nama;
        this.harga = harga;
        this.size = size;
        this.jumlah = jumlah;
    }

    public CartItem(int id_pemesanan, int id_barang, String nama, int harga, String size, int jumlah) {
        this.id_pemesanan = id_pemesanan;
        this.id_barang = id_barang;
        this.nama = nama;
        this.harga = harga;
        this.size = size;
        this.jumlah = jumlah;
    }


    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id_barang", id_barang);
            obj.put("nama", nama);
            obj.put("jumlah", jumlah);
            obj.put("harga", harga);

        } catch (JSONException e) {

        }
        return obj;
    }

    public int getId_pemesanan() {
        return id_pemesanan;
    }

    public void setId_pemesanan(int id_pemesanan) {
        this.id_barang = id_pemesanan;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
