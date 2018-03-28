package com.example.enjoy.enjoycamera;

import android.hardware.Camera;
import android.util.Log;

import com.example.enjoy.enjoycamera.Utils.FaceEvent;

import org.greenrobot.eventbus.EventBus;

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
    private Camera.FaceDetectionListener mFaceDetectionListener = new Camera.FaceDetectionListener() {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            Log.d(TAG,"faces.length ="+ faces.length);
            if(faces.length > 0 ){
                EventBus.getDefault().post(new FaceEvent(faces));
            }else{
                EventBus.getDefault().post(new FaceEvent(null));
            }
        }
    };
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
        List<String> focusModes = mParameters.getSupportedFocusModes();
        if(focusModes.contains(Camera.Parameters.FLASH_MODE_AUTO)){
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.setFaceDetectionListener(mFaceDetectionListener);
        mCamera.setParameters(mParameters);
    }
}
