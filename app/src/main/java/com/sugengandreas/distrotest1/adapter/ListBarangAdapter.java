package com.sugengandreas.distrotest1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sugengandreas.distrotest1.R;
import com.sugengandreas.distrotest1.model.Barang;
import com.sugengandreas.distrotest1.network.VolleySingleton;

import java.util.ArrayList;

import static com.sugengandreas.distrotest1.extras.URLEndPoints.GAMBAR_URL;

public class ListBarangAdapter extends BaseAdapter {

    private VolleySingleton volleySingleton;
    private Context context;
    private ArrayList<Barang> listItems;
    private ImageLoader imageLoader;
    private ImageView listImg;
    private TextView listText;

    public ListBarangAdapter(Context context, ArrayList<Barang> listItems) {
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
            convertView = mInflater.inflate(R.layout.grid_item_with_image, null);
        }

        listImg = (ImageView) convertView.findViewById(R.id.listImage);
        listText = (TextView) convertView.findViewById(R.id.listText);
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

