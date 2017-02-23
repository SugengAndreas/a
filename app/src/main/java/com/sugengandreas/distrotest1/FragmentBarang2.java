package com.sugengandreas.distrotest1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sugengandreas.distrotest1.adapter.ListBarangAdapter;
import com.sugengandreas.distrotest1.model.Barang;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sugengandreas.distrotest1.extras.URLEndPoints.BARANG_URL;

/**
 * Created by Sander on 2/24/2016.
 */
public class FragmentBarang2 extends android.support.v4.app.Fragment {
    public static final String TAG = FragmentBarang2.class.getSimpleName();

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Barang> Barang;
    private ListBarangAdapter adapter;
    private GridView listBarang;
    private int kategori;

    public static FragmentBarang2 newInstance() {
        return new FragmentBarang2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_barang, container, false);
        listBarang = (GridView) v.findViewById(R.id.listBarang);

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        kategori = 2;




        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading",
                "Silakan tunggu", false, false);
        StringRequest request = new StringRequest(Request.Method.POST,
                BARANG_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    final JSONObject json = new JSONObject(response);
                    if (!json.has("error")) {
                        Barang = new ArrayList<>();
                        Barang = parseHasil(json);
                        adapter = new ListBarangAdapter(getActivity(),
                                Barang);
                        listBarang.setAdapter(adapter);

                        listBarang.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(
                                    AdapterView<?> parent,
                                    View view, int position,
                                    long id) {

                                Barang b = Barang.get(position);
                                Intent i = new Intent(getActivity(), ActivityDetailBarang.class);
                                i.putExtra("Barang", b);
                                startActivity(i);

                            }
                        });

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
                loading.dismiss();
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_kategori", kategori+"");
                return params;
            }
        };
        requestQueue.add(request);
    }

    private ArrayList<Barang> parseHasil(JSONObject json) {
        ArrayList<Barang> lists = new ArrayList<>();
        try {
            JSONArray array = json.getJSONArray("barang");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Barang h = new Barang(obj.getInt("id_barang"),
                        obj.getString("nama"),
                        obj.getString("deskripsi"),
                        obj.getInt("harga"),
                        obj.getString("size"),
                        obj.getString("gambar"),
                        obj.getInt("id_kategori"),
                        obj.getInt("stok"));
                lists.add(h);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }


}

