package com.stanvolcere.reversigame;

import android.widget.ImageView;

/**
 * Created by Stan Wayne Volcere on 11/14/2017.
 */

public class Tile {


    int position;
    ImageView image;
    //iamge ids are -1, 0, 1 representing white, empty and black repectively


    public Tile(int position, ImageView image) {
        this.position = position;
        this.image = image;
    }

    //getters and setters
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
