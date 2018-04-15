package com.example.enjoy.enjoycamera;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.enjoy.enjoycamera.Utils.CameraManager;
import com.example.enjoy.enjoycamera.Utils.CustomView;
import com.example.enjoy.enjoycamera.Utils.FaceEvent;
import com.example.enjoy.enjoycamera.Utils.FaceView;
import com.example.enjoy.enjoycamera.Utils.FileUtils;
import com.example.enjoy.enjoycamera.Utils.MessageEvent;
import com.viewpagerindicator.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private FrameLayout mPreview = null;
    private Camera mCamera = null;
    private TabPageIndicator mTabPageIndicator;
    private FaceView mFaceView;
    private static final int CAMERA_MODE_VIDEO = 2;
    private static final int CAMERA_MODE_PHOTO = 3;
    private static final int CAMERA_MODE_SETTINGS = 9;
    private int mMode = 3;
    private List<ModeItem> modeList = new ArrayList<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        Log.d(TAG,"onMessageEvent");
        String filePath = messageEvent.getMessage();
        int targetW = mIvThumbnail.getWidth();
        int targetH = mIvThumbnail.getHeight();
        Bitmap bitmap = FileUtils.getBitmapFromFile(filePath,targetW,targetH);

        mIvThumbnail.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFaceEvent(FaceEvent faceEvent){
        Log.d(TAG,"onFaceEvent");
        Camera.Face[] faces = faceEvent.getFaces();
        mFaceView.setFace(faces);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_thumbnail:
                break;
            case R.id.iv_capture:
                if(mMode == CAMERA_MODE_PHOTO){
                    takePicture();
                }else if(mMode == CAMERA_MODE_VIDEO){
                    takeVideo();
                }
                break;
            case R.id.iv_switch:
                mCameraManager.switchCamera();
                break;
            case R.id.fl_cameraPreview:
                Log.d(TAG,"click camera");
                mCameraManager.autoFocus();
                break;
                default:
                    break;
        }
    }

    private void takePicture(){
        boolean enable = preference.getBoolean("switch_preference_shutter_sound",false);
        Log.d(TAG,"enable = "+enable);
        mPhotoMode.setPictureSize((double)4/3);
        mPhotoMode.takePicture(enable);
    }

    private void takeVideo(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
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
        //mTabPageIndicator.onPageSelected(mMode);
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
        //mVideoMode = new VideoMode(mCamera,mCameraPreview);
        mPreview = findViewById(R.id.fl_cameraPreview);
        mPreview.setOnClickListener(this);
        mPhotoMode = new PhotoMode(this,mCamera,mPreview);
        mPhotoMode.init();
        mCameraManager = new CameraManager(mPhotoMode.getHolder(),mPhotoMode);
        mFaceView = new FaceView(this);
        addContentView(mFaceView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        mGridLine = new CustomView(this);
        addContentView(mGridLine,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        mIvCapture = findViewById(R.id.iv_capture);
        mIvCapture.setOnClickListener(this);
        mIvSwitch = findViewById(R.id.iv_switch);
        mIvSwitch.setOnClickListener(this);
        mIvThumbnail = findViewById(R.id.iv_thumbnail);
        mIvThumbnail.setOnClickListener(this);

        initMode();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        ModeAdapter adapter = new ModeAdapter(modeList);
        recyclerView.setAdapter(adapter);
       /* ViewPager viewPager = findViewById(R.id.pager);
        MyAdapter myAdapter = new MyAdapter(CameraActivity.this,mCameraManager);
        viewPager.setAdapter(myAdapter);

        mTabPageIndicator = findViewById(R.id.indicator);
        mTabPageIndicator.setViewPager(viewPager);
        mTabPageIndicator.onPageSelected(CAMERA_MODE_PHOTO);
        mTabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG,"onPageSelected position ="+position);
                if(position != CAMERA_MODE_SETTINGS){
                    mMode = position;
                }
                mPreview.removeAllViews();
                switch (position){
                    case CAMERA_MODE_PHOTO:
                        mVideoMode = null;
                        mPhotoMode = new PhotoMode(getApplicationContext(),mCamera,mPreview);
                        mPhotoMode.init();
                        break;
                    case CAMERA_MODE_VIDEO:
                        mPhotoMode = null;
                        mVideoMode = new VideoMode(getApplicationContext(),mCamera,mPreview);
                        mVideoMode.init();
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
        });*/
    }
    private void initMode(){
        ModeItem delayRec = new ModeItem("延时录像",R.mipmap.ic_launcher);
        modeList.add(delayRec);

        ModeItem slowRec = new ModeItem("慢动作录像",R.mipmap.ic_launcher);
        modeList.add(slowRec);

        ModeItem rec = new ModeItem("录像",R.mipmap.ic_launcher);
        modeList.add(rec);

        ModeItem takePhoto = new ModeItem("拍照",R.mipmap.ic_launcher);
        modeList.add(takePhoto);

        ModeItem blur = new ModeItem("背景虚化",R.mipmap.ic_launcher);
        modeList.add(blur);

        ModeItem panora = new ModeItem("全景",R.mipmap.ic_launcher);
        modeList.add(panora);

        ModeItem profession = new ModeItem("专业相机",R.mipmap.ic_launcher);
        modeList.add(profession);

        ModeItem scan = new ModeItem("扫描",R.mipmap.ic_launcher);
        modeList.add(scan);

        ModeItem scanCard = new ModeItem("扫名片",R.mipmap.ic_launcher);
        modeList.add(scanCard);

        ModeItem settings = new ModeItem("更多设置",R.mipmap.ic_launcher);
        modeList.add(settings);

    }
    private void gotoSetting(){
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
}
