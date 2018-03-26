package com.example.enjoy.enjoycamera;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import com.example.enjoy.enjoycamera.Utils.FileUtils;

import java.io.IOException;

/**
 * Created by libo on 2018/3/26.
 */

public class VideoMode {
    private MediaRecorder mMediaRecorder = null;
    private boolean isRecording = false;
    private Camera mCamera = null;
    private CameraPreview mCameraPreview = null;

    public VideoMode(Camera camera, CameraPreview cameraPreview) {
        this.mCamera = mCamera;
        this.mCameraPreview = mCameraPreview;
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
