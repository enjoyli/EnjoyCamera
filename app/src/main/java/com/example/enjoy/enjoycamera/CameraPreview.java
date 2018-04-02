package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by admin on 2018/03/19   .
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String  TAG = "CameraPreview";
    private SurfaceHolder mHolder = null;
    private Camera.Parameters mParameters = null;
    public static int sSurfaceViewW = 0;
    public static int sSurfaceViewH = 0;
    private PhotoController mPhotoController;


    public CameraPreview(Context context, PhotoController photoController) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPhotoController = photoController;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"surfaceCreated w ="+this.getWidth());
        sSurfaceViewW = this.getWidth();
        sSurfaceViewH = this.getHeight();
        mPhotoController.previewReady();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        if(surfaceHolder.getSurface()==null){
            return;
        }
        sSurfaceViewW = w;
        sSurfaceViewH = h;
        mPhotoController.previewChanged();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mPhotoController.previewDestroyed();
    }

}
