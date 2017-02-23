package com.sugengandreas.distrotest1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.sugengandreas.distrotest1.extras.URLEndPoints.KONFIRMASI_URL;

/**
 * Created by Sander on 2/24/2016.
 */
public class FragmentKonfirmasi extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private ArrayList<String> Bank;
    private String namaBank;
    private SpinnerAdapter adapter;
    private Button btnPilih;
    private ImageView imgBukti;
    private String sudahPilihGambar, PilihGambar = "", Nama,idPemesanan, img, Alamat;
    private EditText editNama;

    public static final String TAG = FragmentKonfirmasi.class.getSimpleName();

    public static FragmentKonfirmasi newInstance() {
        return new FragmentKonfirmasi();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_konfirmasi, container, false);

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        Nama = prefs.getString("MemberNama", "");
        idPemesanan = prefs.getString("PemesananId", "");
        Alamat = prefs.getString("MemberAlamat", "");


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String tanggal = df.format(Calendar.getInstance().getTime());
        Spinner spBank = (Spinner) v.findViewById(R.id.spinnerBank);
        Bank = new ArrayList<>(Arrays.asList(getResources().getStringArray(
                R.array.nama_bank)));
        adapter = new SpinnerAdapter(getActivity(), Bank);
        spBank.setAdapter(adapter);
        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                namaBank = Bank.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                namaBank = "";
            }
        });
        editNama = (EditText) v.findViewById(R.id.editNama);
        imgBukti = (ImageView) v.findViewById(R.id.imgBukti);
        btnPilih = (Button) v.findViewById(R.id.btnTambah);
        btnPilih.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Pilih Gambar"),
                        PICK_IMAGE_REQUEST);

            }
        });

        Button btnSelesai = (Button) v.findViewById(R.id.btnSelesai);
        btnSelesai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String nama = editNama.getText().toString();
              Toast.makeText(getActivity(),
                        "id_pemesanan="+idPemesanan+".Nama="+Nama+"atas nama="+nama+".tanggal="+tanggal+".alamat="+Alamat+".Namabank="+namaBank+img,
                        Toast.LENGTH_LONG).show();


                final ProgressDialog loading = ProgressDialog.show(
                        getActivity(), "Loading", "Silakan tunggu",
                        false, false);
                if (!nama.isEmpty() && !sudahPilihGambar.isEmpty()) {
                    img = getStringImage(bitmap);
                    StringRequest request = new StringRequest(
                            Request.Method.POST, KONFIRMASI_URL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();
                                    try {
                                        final JSONObject json = new JSONObject(response);
                                        if (!json.has("error")) {

                                            Toast.makeText(getActivity(),
                                                   "Data Konfirmasi Telah Ditambah",
                                                    Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(
                                                    getActivity(),
                                                    ActivityMainwithLogin.class);

                                            startActivity(i);

                                            startActivity(i);


                                        } else {
                                            Toast.makeText(getActivity(),
                                                    json.getString("error"),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getActivity(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams()
                                throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_pemesanan", idPemesanan);
                            params.put("nama", Nama);
                            params.put("tanggal", tanggal);
                            params.put("alamat", Alamat);
                            params.put("bank_tujuan", namaBank);
                            params.put("atas_nama", nama);
                            params.put("bukti_transfer", img);
                            return params;
                        }
                    };
                    requestQueue.add(request);

                } else {

                    AlertDialog pesan = new AlertDialog.Builder(
                            getActivity()).create();
                    pesan.setTitle("Kesalahan");
                    pesan.setMessage("Form wajib diisi!");
                    pesan.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.dismiss();
                                }
                            });
                    pesan.show();
                }
            }
        });
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                // Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                // Setting the Bitmap to ImageView
                imgBukti.setVisibility(View.VISIBLE);
                imgBukti.setImageBitmap(bitmap);
                sudahPilihGambar = "sudah";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SpinnerAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> listItems;

        public SpinnerAdapter(Context context, ArrayList<String> listItems) {
            super();
            this.context = context;
            this.listItems = listItems;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.spinner_item, null);
            }

            TextView txtBank = (TextView) convertView
                    .findViewById(R.id.txtSpinner);

            txtBank.setText(listItems.get(position));

            return convertView;
        }
    }
    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
