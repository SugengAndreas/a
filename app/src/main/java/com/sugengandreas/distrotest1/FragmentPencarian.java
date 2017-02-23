package com.sugengandreas.distrotest1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.sugengandreas.distrotest1.model.Barang;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sugengandreas.distrotest1.extras.URLEndPoints.BARANG_URL;
import static com.sugengandreas.distrotest1.extras.URLEndPoints.GAMBAR_URL;
import static com.sugengandreas.distrotest1.extras.URLEndPoints.PENCARIAN_URL;


public class FragmentPencarian extends android.support.v4.app.Fragment {
    public static final String TAG = FragmentPencarian.class.getSimpleName();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private EditText editCari;
    private TextView txtNotFound;
    private ListView listHasil;
    private ArrayList<Barang> Barang;
    private ListAdapter adapter;
    private int kategori;
    private String cari;

    public static FragmentPencarian newInstance() {
        return new FragmentPencarian();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pencarian, container, false);
        txtNotFound = (TextView) v.findViewById(R.id.txtNotFound);

        listHasil = (ListView) v.findViewById(R.id.listHasilPencarian);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();


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
                        adapter = new ListAdapter(getActivity(),
                                Barang);
                        listHasil.setAdapter(adapter);

                        listHasil.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                params.put("id_kategori", 0 + "");
                return params;
            }
        };
        requestQueue.add(request);


        editCari = (EditText) v.findViewById(R.id.editSearch);
        editCari.setFocusableInTouchMode(true);
        ImageView btnCari = (ImageView) v.findViewById(R.id.btnSearch);
        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cari = "";
                hideKeyboard(getContext());
                if (!editCari.getText().toString().isEmpty()) {
                    cari = editCari.getText().toString();
                }
                final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading",
                        "Silakan tunggu", false, false);
                StringRequest request = new StringRequest(Request.Method.POST,
                        PENCARIAN_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            final JSONObject json = new JSONObject(response);
                            if (!json.has("error")) {
                                Barang = new ArrayList<>();
                                Barang = parseHasil(json);
                                adapter = new ListAdapter(getActivity(),
                                        Barang);
                                listHasil.setAdapter(adapter);

                                listHasil.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> parent,
                                            View view, int position,
                                            long id) {

                                        Barang g = Barang.get(position);
                                        Intent i = new Intent(getActivity(), ActivityDetailBarang.class);
                                        i.putExtra("Barang", g.getId());
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
                        params.put("nama", cari);
                        return params;
                    }
                };
                requestQueue.add(request);


                if (!Barang.isEmpty()) {
                    txtNotFound.setVisibility(View.INVISIBLE);

                } else {
                    txtNotFound.setVisibility(View.VISIBLE);
                }

            }
        });
        return v;
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


    public class ListAdapter extends BaseAdapter {

        private VolleySingleton volleySingleton;
        private ImageLoader imageLoader;
        private Context context;
        private ArrayList<Barang> listItems;

        public ListAdapter(Context context, ArrayList<Barang> listItems) {
            super();
            this.context = context;
            this.listItems = listItems;
            volleySingleton = VolleySingleton.getInstance();
            imageLoader = volleySingleton.getImageLoader();
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
                convertView = mInflater.inflate(R.layout.list_item_with_image, null);
            }

            final ImageView listImg = (ImageView) convertView.findViewById(R.id.listImage);
            TextView listText = (TextView) convertView.findViewById(R.id.listText);
            String posterURL = GAMBAR_URL + listItems.get(position).getGambar();
            imageLoader.get(posterURL, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    listImg.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            listText.setText(listItems.get(position).getNama());

            return convertView;
        }


    }
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}