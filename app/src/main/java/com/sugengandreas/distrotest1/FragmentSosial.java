package com.sugengandreas.distrotest1;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sander on 2/24/2016.
 */
public class FragmentSosial extends android.support.v4.app.Fragment {
    private TextView Facebook, Twitter, Instagram;

    public static final String TAG = FragmentSosial.class.getSimpleName();

    public static FragmentSosial newInstance() {
        return new FragmentSosial();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sosial, container, false);

        Facebook = (TextView) v.findViewById(R.id.txtFacebook);
        Twitter = (TextView) v.findViewById(R.id.txtTwitter);
        Instagram = (TextView) v.findViewById(R.id.txtInstagram);


        Facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.facebook.com"));
                startActivity(intent);

            }
        });
        Twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.twitter.com"));
                startActivity(intent);

            }
        });
        Instagram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.instagram.com"));
                startActivity(intent);

            }
        });


        return v;
    }
}
