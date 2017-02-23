package com.sugengandreas.distrotest1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sugengandreas.distrotest1.model.Barang;
import com.sugengandreas.distrotest1.model.CartItem;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.sugengandreas.distrotest1.extras.URLEndPoints.PEMESANAN_URL;

/**
 * Created by Sander on 2/24/2016.
 */
public class FragmentCart extends android.support.v4.app.Fragment {
    public static final String TAG = FragmentCart.class.getSimpleName();

    Context context = getActivity();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ListView listCart;
    private DatabaseHandler db;
    private ListAdapter adapter;
    private ArrayList<CartItem> listItems;
    private TextView txtNotFound;
    private Button btnCheckOut;
    private int hasil, id_pemesanan;
    private JSONArray JSONArray;
    private String Barang, idMember, tanggal, idPemesanan;

    public static FragmentCart newInstance() {
        return new FragmentCart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        txtNotFound = (TextView) v.findViewById(R.id.txtNotFound);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        idMember = prefs.getString("MemberId", "");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tanggal = df.format(Calendar.getInstance().getTime());

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        db = new DatabaseHandler(getActivity());
        listCart = (ListView) v.findViewById(R.id.listCart);
        btnCheckOut = (Button) v.findViewById(R.id.btnCheckOut);
        listItems = new ArrayList<>();



        return v;
    }
  /*  public void onBackPressed() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Harap memberikan Komentar dan  mengisi Rating terlebih dahulu");
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/


    @Override
    public void onResume() {
        super.onResume();
        listItems = (ArrayList<CartItem>) db.daftarCartItem();
        adapter = new ListAdapter(getActivity(), listItems);
        listCart.setAdapter(adapter);
        hasil = db.totalHarga();

        listCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CartItem ci = listItems.get(position);
                db.deleteBarang(ci.getId_pemesanan());
                Toast.makeText(getActivity(),
                        "Barang sudah dihapus dari cart",
                        Toast.LENGTH_LONG).show();
                onResume();
            }
        });

        JSONArray = new JSONArray();
        for (int i = 0; i < listItems.size(); i++) {
            JSONArray.put(listItems.get(i).getJSONObject());
        }
        Barang = JSONArray.toString();

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading",
                        "Silakan tunggu", false, false);
                StringRequest request = new StringRequest(Request.Method.POST,
                        PEMESANAN_URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            final JSONObject json = new JSONObject(response);
                            if (!json.has("error")) {
                                SharedPreferences prefs = PreferenceManager
                                        .getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor ed = prefs
                                        .edit();
                                ed.putString("PemesananId",
                                        json.getString("id_pemesanan"));
                                ed.commit();

                                Toast.makeText(getActivity(),
                                        "Pemesanan akan segera diproses",
                                        Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(), ActivityMainwithLogin.class);
                                startActivity(i);
                                db.deleteSemuaBarang();

                            } else {
                                Toast.makeText(getActivity(),
                                        "error",
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
                        params.put("id_member", idMember + "");
                        params.put("barang", Barang + "");
                        params.put("tanggal", tanggal);
                        params.put("total_harga", hasil + "");
                        return params;
                    }
                };
                requestQueue.add(request);

            }
        });

        if (!listItems.isEmpty()) {
            txtNotFound.setVisibility(View.INVISIBLE);
            btnCheckOut.setVisibility(View.VISIBLE);

        } else {
            txtNotFound.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.INVISIBLE);
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<CartItem> listItems;

        public ListAdapter(Context context, ArrayList<CartItem> listItems) {
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
                convertView = mInflater.inflate(R.layout.list_item, null);
            }
            TextView listText = (TextView) convertView
                    .findViewById(R.id.listNama);
            TextView listHarga = (TextView) convertView
                    .findViewById(R.id.listHarga);
            listText.setText(position + 1 + " " + listItems.get(position).getNama());
            listHarga.setText(listItems.get(position).getHarga() + "");
            return convertView;
        }

    }
}


