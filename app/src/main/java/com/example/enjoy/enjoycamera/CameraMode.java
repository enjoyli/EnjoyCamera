package com.example.enjoy.enjoycamera;

import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by admin on 2018/03/27   .
 */

public class CameraMode {
    private static final String TAG = "CameraMode";
    public Camera mCamera = null;
    public Camera.Parameters mParameters = null;
    public CameraMode(Camera camera) {
        this.mCamera = camera;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

    public Camera.Size getOptimalSize(String type, double targetRatio){
        final double ASPECT_TOLERANCE = 0.1;
        double minDiff = 0.1;
        int targetHeight = 0;
        Camera.Size optimalSize =null;
        List<Camera.Size> sizeList = null;

        mParameters = mCamera.getParameters();
        if (type == "Picture"){
            sizeList = mParameters.getSupportedPictureSizes();
        }else {
            sizeList = mParameters.getSupportedPreviewSizes();
        }
        for(Camera.Size size : sizeList){
            double ratio = (double)size.width/size.height;
            Log.d(TAG,"size w ="+size.width+" size h ="+size.height);
            if(Math.abs(targetRatio - ratio) > ASPECT_TOLERANCE){
                continue;
            }

            if(Math.abs(targetHeight - size.height) > minDiff){
                optimalSize = size;
                minDiff = Math.abs(targetHeight - size.height);
            }
        }
        return optimalSize;
    }

    public void setPreviewSize(double targetRatio){
        Camera.Size previewSize = getOptimalSize("Preview",targetRatio);
        Log.d(TAG,"preview H ="+previewSize.height+" preview W ="+previewSize.width);
        mParameters.setPreviewSize(previewSize.width,previewSize.height);
        mCamera.setParameters(mParameters);
    }
}
