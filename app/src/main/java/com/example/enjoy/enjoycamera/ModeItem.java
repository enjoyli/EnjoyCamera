package com.example.enjoy.enjoycamera;

/**
 * Created by admin on 2018/04/15   .
 */

public class ModeItem {
    private String name;
    private int imageId;

    public ModeItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
