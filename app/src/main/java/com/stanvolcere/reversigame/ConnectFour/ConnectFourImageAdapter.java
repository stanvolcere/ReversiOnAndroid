package com.stanvolcere.reversigame.ConnectFour;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.stanvolcere.reversigame.R;

/**
 * Created by stanvolcere on 07/02/2018.
 */
    public class ConnectFourImageAdapter extends BaseAdapter {

    private Context mContext;
    public ConnectFourImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        //return mThumbIds.length;
        return 64;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 3, 0, 0);

//            if (position == 27 || position == 36){
//                imageView.setImageResource(mThumbIds[1]);
//            }
//            else if (position == 28 || position == 35){
//                imageView.setImageResource(mThumbIds[2]);
//            }
//            else {
//                imageView.setImageResource(mThumbIds[0]);
//            }
            imageView.setImageResource(mThumbIds[0]);

        } else {
            imageView = (ImageView) convertView;
        }

        return imageView;
    }


    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.boardcolour,
            R.drawable.blackchip,
            R.drawable.whitechip
    };



}

