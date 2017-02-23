package com.sugengandreas.distrotest1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;


/**
 * Created by Sander on 2/23/2016.
 */
public class ActivitySplash extends Activity {
    String Sesi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
         Sesi = prefs.getString("MemberId", "");

        // Loading selama 3 detik.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if(Sesi!="") {
                    Intent i = new Intent(
                            ActivitySplash.this,
                            ActivityMainwithLogin.class

                    );

                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(
                            ActivitySplash.this,
                            ActivityMain.class

                    );

                    startActivity(i);
                    finish();}
            }
        }, 3 * 1000);


    }
}
