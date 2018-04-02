package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.enjoy.enjoycamera.Utils.FileUtils;
import com.example.enjoy.enjoycamera.Utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2018/03/27   .
 */

public class PhotoMode extends CameraMode implements PhotoController{
    private static final String TAG = "PhotoMode";
    private CameraPreview mCameraPreview = null;
    private Context mContext;
    public PhotoMode(Context context, Camera camera, ViewGroup parent) {
        super(camera);
        mContext = context;
        this.mCameraPreview = new CameraPreview(context,this);
        parent.addView(mCameraPreview);
    }

    public SurfaceHolder getHolder(){
        return mCameraPreview.getHolder();
    }

    public void init(){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mCameraPreview.getLayoutParams();
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        params.width = wm.getDefaultDisplay().getWidth();
        float tempH = params.width * (float)(4.0/3.0);
        params.height = (int)tempH;
        Log.d(TAG,"params w ="+params.width+" H ="+params.height);
        mCameraPreview.setLayoutParams(params);
    }

    @Override
    public void previewReady() {
        setPreviewSize((double)4/3);
        try {
            mCamera.setPreviewDisplay(mCameraPreview.getHolder());
            mCamera.startPreview();
            mCamera.startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void previewChanged() {
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mCameraPreview.getHolder());
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            mCamera.startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void previewDestroyed() {
        if(mCamera!=null){
            mCamera.stopPreview();
        }
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mPicture = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File pictureFile = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                return;
            }
            String filePath = pictureFile.getAbsolutePath();
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
                EventBus.getDefault().post(new MessageEvent(filePath));
                mCamera.startPreview();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    public void takePicture(boolean playShutterSound){
        if(playShutterSound) {
            mCamera.takePicture(mShutterCallback, null, mPicture);
        }else{
            mCamera.takePicture(null, null, mPicture);
        }
    }

    public void setPictureSize(double targetRatio){
        mParameters = mCamera.getParameters();
        Camera.Size pictureSize = getOptimalSize("Picture",targetRatio);

        mParameters.setPictureSize(pictureSize.width,pictureSize.height);
        mParameters.set("rotation", 90);

        mCamera.setParameters(mParameters);
    }
}
