package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.enjoy.enjoycamera.Utils.CameraManager;

import java.io.IOException;

/**
 * Created by admin on 2018/03/19   .
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String  TAG = "CameraPreview";
    private Camera mCamera = null;
    private CameraManager mCameraManager = null;
    private SurfaceHolder mHolder = null;
    private Camera.Parameters mParameters = null;

    public CameraPreview(Context context, Camera camera, CameraManager cameraManager) {
        super(context);
        this.mCamera = camera;
        mCameraManager = cameraManager;
        mParameters = mCamera.getParameters();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCameraManager.setPreviewSize((double)16/9);
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        if(surfaceHolder.getSurface()==null){
            return;
        }
        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(mCamera!=null){
            mCamera.stopPreview();
        }
    }
}
