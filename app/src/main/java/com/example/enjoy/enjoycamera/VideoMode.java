package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.enjoy.enjoycamera.Utils.FileUtils;

import java.io.IOException;

/**
 * Created by libo on 2018/3/26.
 */

public class VideoMode extends CameraMode implements PhotoController{
    private static final String TAG = "VideoMode";
    private MediaRecorder mMediaRecorder = null;
    private boolean isRecording = false;
    private CameraPreview mCameraPreview = null;
    private Context mContext;
    public VideoMode(Context context, Camera camera, ViewGroup parent) {
        super(camera);
        this.mContext = context;
        this.mCameraPreview = new CameraPreview(context,this);
        parent.addView(mCameraPreview);
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

    public boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void startVideoRecorder(){
        mMediaRecorder.start();
        isRecording = true;
    }
    public void stopVideoRecorder(){
        mMediaRecorder.stop();
        isRecording = false;
        releaseMediaRecorder();
    }
    public boolean getRecorderState(){
        return isRecording;
    }
    public void releaseMediaRecorder(){
        if(mMediaRecorder!=null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
            isRecording = false;
        }
    }
}
