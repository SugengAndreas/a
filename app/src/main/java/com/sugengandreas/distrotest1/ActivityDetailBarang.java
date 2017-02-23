package com.sugengandreas.distrotest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sugengandreas.distrotest1.model.Barang;
import com.sugengandreas.distrotest1.model.CartItem;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import static com.sugengandreas.distrotest1.extras.URLEndPoints.GAMBAR_URL;


/**
 * Created by Sander on 2/23/2016.
 */
public class ActivityDetailBarang extends AppCompatActivity {
    private TextView txtNama, txtDeskripsi, txtSize, txtHarga;
    private Button btnAdd;
    private DatabaseHandler db;
    private int id;
    private Barang g;
    private ImageView Img;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private String Sesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        Intent i = getIntent();
        g = (Barang) i.getSerializableExtra("Barang");
        db = new DatabaseHandler(this);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Sesi = prefs.getString("MemberId", "");

        txtNama = (TextView) findViewById(R.id.txtNama);
        txtDeskripsi = (TextView) findViewById(R.id.txtDeskripsi);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtHarga = (TextView) findViewById(R.id.txtHarga);
        Img = (ImageView) findViewById(R.id.imgBarang);

        txtNama.setText("Nama : " + g.getNama());
        txtDeskripsi.setText("Deskripsi : " + g.getDeskripsi());
        txtSize.setText("Size: " + g.getSize());
        txtHarga.setText("Harga : " + g.getHarga());
        String imgURL = GAMBAR_URL + g.getGambar();
        Img = (ImageView) findViewById(R.id.imgBarang);
        imageLoader.get(imgURL, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Img.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        btnAdd = (Button) findViewById(R.id.btnAdd);


        if(Sesi!="") {
            btnAdd.setVisibility(View.VISIBLE);
        }else {
            btnAdd.setVisibility(View.INVISIBLE);
            }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.tambahBarang(new CartItem(g.getId(), g.getNama(), g.getHarga(), g.getSize(), 1));
                Toast.makeText(ActivityDetailBarang.this, "Barang Sudah Ditambahkan ke Cart", Toast.LENGTH_LONG).show();
            }
        });

    }

}
