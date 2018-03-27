package com.example.enjoy.enjoycamera;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.enjoy.enjoycamera.Utils.CameraManager;
import com.example.enjoy.enjoycamera.Utils.CustomView;
import com.example.enjoy.enjoycamera.Utils.MessageEvent;
import com.viewpagerindicator.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CameraActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "CameraActivity";
    private ImageView mIvThumbnail = null;
    private ImageView mIvCapture = null;
    private ImageView mIvSwitch = null;
    private SharedPreferences preference;
    private CustomView mGridLine;
    private CameraManager mCameraManager = null;
    private VideoMode mVideoMode = null;
    private PhotoMode mPhotoMode = null;
    private CameraPreview mCameraPreview = null;
    private Camera mCamera = null;
    private static final int CAMERA_MODE_VIDEO = 2;
    private static final int CAMERA_MODE_PHOTO = 3;
    private static final int CAMERA_MODE_SETTINGS = 9;
    private int mMode = 3;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        String filePath = messageEvent.getMessage();
        int targetW = mIvThumbnail.getWidth();
        int targetH = mIvThumbnail.getHeight();

        Log.d(TAG,"w ="+targetW+" H ="+targetH);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        mIvThumbnail.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_thumbnail:
                break;
            case R.id.iv_capture:
                if(mMode == CAMERA_MODE_PHOTO){
                    takePicture();
                }
                break;
            case R.id.iv_switch:
                mCameraManager.switchCamera();
                break;
                default:
                    break;
        }
    }

    private void takePicture(){
        boolean enable = preference.getBoolean("switch_preference_shutter_sound",false);
        Log.d(TAG,"enable = "+enable);
        mPhotoMode.setPictureSize((double)16/9);
        mPhotoMode.takePicture(enable);
    }

    private void takeVideo(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        init();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrHideGridLine();
    }

    private void showOrHideGridLine(){
        if(preference.getBoolean("switch_preference_grid_line",false)){
            mGridLine.setVisibility(View.VISIBLE);
        }else{
            mGridLine.setVisibility(View.INVISIBLE);
        }
    }

    private void init(){
        mCamera = CameraManager.getCameraInstance();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void initView(){
        Log.d(TAG,"initView");
        mPhotoMode = new PhotoMode(mCamera);
        mCameraPreview = new CameraPreview(this,mPhotoMode);
        mVideoMode = new VideoMode(mCamera,mCameraPreview);
        mCameraManager = new CameraManager(mCameraPreview,mPhotoMode);
        FrameLayout preview = findViewById(R.id.fl_cameraPreview);
        preview.addView(mCameraPreview);
        mGridLine = new CustomView(this);
        addContentView(mGridLine,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        mIvCapture = findViewById(R.id.iv_capture);
        mIvCapture.setOnClickListener(this);
        mIvSwitch = findViewById(R.id.iv_switch);
        mIvSwitch.setOnClickListener(this);
        mIvThumbnail = findViewById(R.id.iv_thumbnail);
        mIvThumbnail.setOnClickListener(this);

        ViewPager viewPager = findViewById(R.id.pager);
        MyAdapter myAdapter = new MyAdapter(CameraActivity.this,mCameraManager);
        viewPager.setAdapter(myAdapter);

        TabPageIndicator tabPageIndicator = findViewById(R.id.indicator);
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.onPageSelected(CAMERA_MODE_PHOTO);
        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG,"onPageSelected position ="+position);
                mMode = position;
                switch (position){
                    case CAMERA_MODE_PHOTO:
                        break;
                    case CAMERA_MODE_VIDEO:
                        break;
                    case CAMERA_MODE_SETTINGS:
                        gotoSetting();
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void gotoSetting(){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
}
