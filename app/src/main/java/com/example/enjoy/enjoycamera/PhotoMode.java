package com.example.enjoy.enjoycamera;

import android.hardware.Camera;

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

public class PhotoMode extends CameraMode{

    public PhotoMode(Camera camera) {
        super(camera);
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
