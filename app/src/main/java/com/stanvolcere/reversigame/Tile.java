package com.stanvolcere.reversigame;

/**
 * Created by Stan Wayne Volcere on 11/14/2017.
 */

public class Tile {

    int minimaxValue;
    //iamge ids are -1, 0, 1 representing white, empty and black repectively
    int imageId;

    public Tile(int imageId) {
        //this.minimaxValue = value;
        this.imageId = imageId;
    }

    //getters and setters
    public int getMinimaxValue() {
        return minimaxValue;
    }

    public void setMinimaxValue(int value) {
        this.minimaxValue = value;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
