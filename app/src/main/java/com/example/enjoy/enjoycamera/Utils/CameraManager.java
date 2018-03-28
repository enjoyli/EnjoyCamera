package com.example.enjoy.enjoycamera.Utils;

import android.hardware.Camera;

import com.example.enjoy.enjoycamera.CameraMode;
import com.example.enjoy.enjoycamera.CameraPreview;

import java.io.IOException;

/**
 * Created by admin on 2018/03/21   .
 */

public class CameraManager {
    private static final String TAG = "CameraManager";
    private static Camera mCamera = null;
    private static int mOpenCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private CameraPreview mCameraPreview = null;
    private CameraMode mCameraMode = null;

    public CameraManager() {
    }

    public CameraManager(CameraPreview cameraPreview, CameraMode cameraMode) {
        this.mCameraPreview = cameraPreview;
        this.mCameraMode = cameraMode;
    }

    public static Camera getCameraInstance(){
        if (mCamera == null){
            mCamera = Camera.open();
            mOpenCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        return mCamera;
    }
    private boolean safeCameraOpen(int cameraId){
        boolean qOpened = false;
        mCamera = Camera.open(cameraId);
        mCameraMode.setCamera(mCamera);
        qOpened = (mCamera!=null);
        return qOpened;
    }

    private void releaseCameraAndPreview(){
        if(mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }

    public void switchCamera(){
        releaseCameraAndPreview();
        if(mOpenCamId == Camera.CameraInfo.CAMERA_FACING_BACK){
            mOpenCamId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else{
            mOpenCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        safeCameraOpen(mOpenCamId);
        try {
            mCamera.setPreviewDisplay(mCameraPreview.getHolder());
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autoFocus(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){

                }
            }
        });
    }

    public void startFaceDetection(){
        Camera.Parameters parameters = mCamera.getParameters();
        if(parameters.getMaxNumDetectedFaces() > 0){
            mCamera.startFaceDetection();
        }
    }
}
