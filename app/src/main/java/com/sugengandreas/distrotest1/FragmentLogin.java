package com.sugengandreas.distrotest1;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import static com.sugengandreas.distrotest1.extras.URLEndPoints.LOGIN_URL;

public class FragmentLogin extends Fragment {
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private EditText editUser, editPass;
    private Button btnLogin, btnSignUp;
    public static final String TAG = FragmentLogin.class.getSimpleName();

    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        editUser = (EditText) v.findViewById(R.id.editUser);
        editPass = (EditText) v.findViewById(R.id.editPassword);


        btnSignUp = (Button) v.findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FragmentRegister fr = new FragmentRegister();
                ft.replace(R.id.content_frame, fr);
                ft.commit();

            }
        });
        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(getContext());
                final String username = editUser.getText().toString();
                final String pass = editPass.getText().toString();
                if (!username.isEmpty() && !pass.isEmpty()) {
                    final ProgressDialog loading = ProgressDialog.show(
                            getActivity(), "Loading", "Silakan tunggu",
                            false, false);
                    StringRequest request = new StringRequest(
                            Request.Method.POST, LOGIN_URL,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    JSONObject json;
                                    try {
                                        json = new JSONObject(response);
                                        if (json.has("error")) {
                                            Toast.makeText(getActivity(),
                                                    json.getString("error"),
                                                    Toast.LENGTH_LONG).show();
                                            editUser.setText("");
                                            editPass.setText("");
                                            loading.dismiss();

                                        } else {
                                            SharedPreferences prefs = PreferenceManager
                                                    .getDefaultSharedPreferences(getActivity());
                                            SharedPreferences.Editor ed = prefs
                                                    .edit();
                                            ed.putString("MemberId",
                                                    json.getString("id_member"));
                                            ed.putString("MemberNama",
                                                    json.getString("nama"));
                                            ed.putString("MemberAlamat",
                                                    json.getString("alamat"));
                                            ed.commit();
                                            Toast.makeText(getActivity(),
                                                    "Login sukses",
                                                    Toast.LENGTH_LONG).show();
                                            loading.dismiss();
                                            Intent i = new Intent(
                                                    getActivity(),
                                                    ActivityMainwithLogin.class
                                            );
                                            startActivity(i);
                                            getActivity().finish();

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
                            params.put("tipe", "member");
                            params.put("username", username);
                            params.put("password", pass);
                            return params;
                        }
                    };
                    requestQueue.add(request);

                } else {
                    Toast.makeText(getActivity(), "Kolom kosong",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
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
