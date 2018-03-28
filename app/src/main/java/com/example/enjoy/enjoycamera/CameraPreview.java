package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by admin on 2018/03/19   .
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String  TAG = "CameraPreview";
    private CameraMode mCameraMode = null;
    private SurfaceHolder mHolder = null;
    private Camera.Parameters mParameters = null;
    public static int sSurfaceViewW = 0;
    public static int sSurfaceViewH = 0;


    public CameraPreview(Context context, CameraMode cameraMode) {
        super(context);
        mCameraMode = cameraMode;
        mParameters = mCameraMode.getCamera().getParameters();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"surfaceCreated w ="+this.getWidth());
        sSurfaceViewW = this.getWidth();
        sSurfaceViewH = this.getHeight();
        mCameraMode.setPreviewSize((double)4/3);
        try {
            mCameraMode.getCamera().setPreviewDisplay(surfaceHolder);
            mCameraMode.getCamera().startPreview();
            mCameraMode.getCamera().startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        if(surfaceHolder.getSurface()==null){
            return;
        }
        sSurfaceViewW = w;
        sSurfaceViewH = h;
        mCameraMode.getCamera().stopPreview();

        try {
            mCameraMode.getCamera().setPreviewDisplay(mHolder);
            mCameraMode.getCamera().setDisplayOrientation(90);
            mCameraMode.getCamera().startPreview();
            mCameraMode.getCamera().startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(mCameraMode.getCamera()!=null){
            mCameraMode.getCamera().stopPreview();
        }
    }

}
