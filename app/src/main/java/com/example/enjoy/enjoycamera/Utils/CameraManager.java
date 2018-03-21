package com.example.enjoy.enjoycamera.Utils;

import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by admin on 2018/03/21   .
 */

public class CameraManager {
    private static final String TAG = "CameraManager";
    private static Camera mCamera = null;
    private Camera.Parameters mParameters = null;

    public CameraManager() {
    }

    public static Camera getCameraInstance(){
        if (mCamera == null){
            mCamera = Camera.open();
        }
        return mCamera;
    }

    public Camera.Size getOptimalSize(String type,double targetRatio){
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

    public void setPictureSize(double targetRatio){
        mParameters = mCamera.getParameters();
        Camera.Size pictureSize = getOptimalSize("Picture",targetRatio);

        mParameters.setPictureSize(pictureSize.width,pictureSize.height);
        mParameters.set("rotation", 90);

        mCamera.setParameters(mParameters);
    }
}
