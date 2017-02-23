package com.sugengandreas.distrotest1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sugengandreas.distrotest1.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sander on 7/28/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "distro";
    private static final String TABEL_BARANG = "barang";
    private static final String TABEL_PEMESANAN = "pemesanan";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTabelBarang = "CREATE TABLE " + TABEL_BARANG +
                "( id_pemesanan integer primary key autoincrement not null, " +
                "id_barang INTEGER, " +
                "nama TEXT, " +
                "harga INTEGER," +
                "size TEXT, " +
                "jumlah INTEGER)";

        db.execSQL(queryTabelBarang);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_BARANG);

    }

    public void tambahBarang(CartItem c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_barang", c.getId_barang());
        values.put("nama", c.getNama());
        values.put("harga", c.getHarga());
        values.put("size", c.getSize());
        values.put("jumlah", c.getJumlah());
        db.insert(TABEL_BARANG, null, values);
        db.close();
    }
    public void deleteBarang(int id) {
                SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABEL_BARANG,"id_pemesanan = ?", new String[]{
                "" + id, });
        db.close();
    }
    public void deleteSemuaBarang() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_BARANG, null, null);
        db.close();
    }
    public List<CartItem> daftarCartItem() {
        List<CartItem> daftar = new ArrayList<>();
        String query = "SELECT * FROM " + TABEL_BARANG + " ORDER BY nama";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                CartItem w = new CartItem(
                        c.getInt(0), c.getInt(1), c.getString(2),
                        c.getInt(3), c.getString(4), c.getInt(5));
                daftar.add(w);
            } while (c.moveToNext());
        }
        return daftar;
    }
    public int totalHarga() {
        int hasil=0 ;
        String query = "SELECT * FROM " + TABEL_BARANG + " ORDER BY nama";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                hasil= hasil+ c.getInt(3);

            } while (c.moveToNext());
        }
        return hasil;
    }

}
