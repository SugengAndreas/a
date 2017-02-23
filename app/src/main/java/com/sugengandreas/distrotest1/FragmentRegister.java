package com.sugengandreas.distrotest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


import static com.sugengandreas.distrotest1.extras.URLEndPoints.REGISTER_URL;

public class FragmentRegister extends Fragment {
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private Button btnLogin;
    private EditText txtNama, txtAlamat, txtTelp, txtEmail, txtUser, txtPass;
    public static final String TAG = FragmentRegister.class.getSimpleName();

    public static FragmentRegister newInstance() {
        return new FragmentRegister();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();


        txtNama = (EditText) v.findViewById(R.id.editNama);
        txtAlamat = (EditText) v.findViewById(R.id.editAlamat);
        txtTelp = (EditText) v.findViewById(R.id.editTelpon);
        txtEmail = (EditText) v.findViewById(R.id.editEmail);
        txtUser = (EditText) v.findViewById(R.id.editUser);
        txtPass = (EditText) v.findViewById(R.id.editPassword);

        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FragmentLogin fl = new FragmentLogin();
                ft.replace(R.id.content_frame, fl);
                ft.commit();

            }
        });


        Button btn1 = (Button) v.findViewById(R.id.btn_Register);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hideKeyboard(getContext());
                                        final String Nama = txtNama.getText().toString();
                                        final String Alamat = txtAlamat.getText().toString();
                                        final String Telpon = txtTelp.getText().toString();
                                        final String Email = txtEmail.getText().toString();
                                        final String User = txtUser.getText().toString();
                                        final String Pass = txtPass.getText().toString();

                                        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                                                try {
                                                    JSONObject json = new JSONObject(response);
                                                    if (json.has("sukses")) {

                                                        Intent i = new Intent(
                                                                getActivity(),
                                                                FragmentLogin.class);
                                                        startActivity(i);

                                                        Toast.makeText(getActivity(), json.getString("sukses"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();


                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("nama", Nama);
                                                map.put("alamat", Alamat);
                                                map.put("nomor_telepon", Telpon);
                                                map.put("email", Email);
                                                map.put("username", User);
                                                map.put("password", Pass);
                                                return map;
                                            }
                                        };

                                        requestQueue.add(request);


                                    }
                                }

        );

        return v;
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
