package com.example.enjoy.enjoycamera;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.enjoy.enjoycamera.Utils.CameraManager;
import com.example.enjoy.enjoycamera.Utils.CustomView;
import com.example.enjoy.enjoycamera.Utils.FileUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    private CameraManager mCameraManager = null;
    private Button mTakePictureBtn = null;
    private Button mSettings = null;
    private CameraPreview mCameraPreview = null;
    private Camera mCamera = null;
    private SharedPreferences preference;
    private CustomView mGridLine;
    private static final String[] demo = new String[]{
            "延时录像", "慢动作录像", "录像", "拍照", "背景虚化", "全景",
            "专业相机", "扫码", "扫名片", "更多设置" };
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return demo.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return demo[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Button button = new Button(CameraActivity.this);
            button.setText(demo[position]);
            button.setGravity(Gravity.CENTER);
            container.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoSetting();
                }
            });
            return button;
        }
    }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(preference.getBoolean("switch_preference_grid_line",false)){
            mGridLine.setVisibility(View.VISIBLE);
        }else{
            mGridLine.setVisibility(View.INVISIBLE);
        }
    }

    private void init(){
        mCameraManager = new CameraManager();
        mCamera = CameraManager.getCameraInstance();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
    }
    private void initView(){
        Log.d(TAG,"initView");
        mCameraPreview = new CameraPreview(this,mCamera, mCameraManager);
        FrameLayout preview = findViewById(R.id.fl_cameraPreview);
        preview.addView(mCameraPreview);
        mGridLine = new CustomView(this);
        addContentView(mGridLine,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
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

        ViewPager viewPager = findViewById(R.id.pager);
        MyAdapter myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter);

        TabPageIndicator tabPageIndicator = findViewById(R.id.indicator);
        tabPageIndicator.setViewPager(viewPager);

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
