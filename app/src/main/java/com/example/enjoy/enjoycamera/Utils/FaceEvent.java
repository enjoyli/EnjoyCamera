package com.example.enjoy.enjoycamera.Utils;

import android.hardware.Camera;

/**
 * Created by admin on 2018/03/28   .
 */

public class FaceEvent {
    private Camera.Face[] mFaces;

    public FaceEvent(Camera.Face[] faces) {
        this.mFaces = faces;
    }

    public Camera.Face[] getFaces() {
        return mFaces;
    }
}
