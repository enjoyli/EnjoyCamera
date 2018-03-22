package com.example.enjoy.enjoycamera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.enjoy.enjoycamera.Utils.CameraManager;
import com.example.enjoy.enjoycamera.Utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private CameraManager mCameraManager = null;
    private Button mTakePictureBtn = null;
    private Button mSettings = null;
    private CameraPreview mCameraPreview = null;
    private Camera mCamera = null;
    private Camera.Parameters mParameters = null;
    private SharedPreferences preference;
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
        setContentView(R.layout.camera_activity);

        //afeCameraOpen();
        init();
        initView();
    }

    private void init(){
        mCameraManager = new CameraManager();
        mCamera = CameraManager.getCameraInstance();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
    }
    private void initView(){
        mCameraPreview = new CameraPreview(this,mCamera, mCameraManager);
        FrameLayout preview = findViewById(R.id.fl_cameraPreview);
        preview.addView(mCameraPreview);
        mSettings = findViewById(R.id.btn_settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSetting();
            }
        });
        mTakePictureBtn = findViewById(R.id.btn_takePicture);
        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enable = preference.getBoolean("switch_preference_shutter_sound",false);
                Log.d(TAG,"enable = "+enable);
                mCameraManager.setPictureSize((double)16/9);
                if(enable) {
                    mCamera.takePicture(mShutterCallback, null, mPicture);
                }else{
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        });

    }

    private void gotoSetting(){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
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
