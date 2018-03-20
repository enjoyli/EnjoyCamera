package com.example.enjoy.enjoycamera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.enjoy.enjoycamera.Utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private Button mTakePictureBtn = null;

    private CameraPreview mCameraPreview = null;
    private Camera mCamera = null;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File pictureFile = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
                mCamera.startPreview();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        safeCameraOpen();
        initView();
    }

    private void initView(){
        mCameraPreview = new CameraPreview(this,mCamera);
        FrameLayout preview = findViewById(R.id.previewFL);
        preview.addView(mCameraPreview);

        mTakePictureBtn = findViewById(R.id.takePictureButton);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null,null,mPicture);
            }
        });

    }

    private boolean safeCameraOpen(){
        boolean qOpened = false;
        releaseCameraAndPreview();
        mCamera = Camera.open();
        qOpened = (mCamera!=null);
        return qOpened;
    }
    private void releaseCameraAndPreview(){
        //mCameraPreview.setCamera(null);
        if(mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }
}
