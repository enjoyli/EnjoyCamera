package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by admin on 2018/03/19   .
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera = null;
    private SurfaceHolder mHolder = null;

    public CameraPreview(Context context, Camera mCamera) {
        super(context);
        this.mCamera = mCamera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
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
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
