package com.example.enjoy.enjoycamera;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.enjoy.enjoycamera.Utils.CameraManager;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by admin on 2018/03/27   .
 */

public class MyAdapter extends PagerAdapter implements IconPagerAdapter {
    private static final String[] CONTENT = new String[]{
            "延时录像", "慢动作录像", "录像", "拍照", "背景虚化", "全景",
            "专业相机", "扫码", "扫名片", "更多设置" };
    private static final int[] ICONS = new int[] {
            R.mipmap.ic_launcher,
    };

    private CameraManager mCameraManager = null;
    private Context mContext = null;
    public MyAdapter(Context context,CameraManager cameraManager) {
        mContext = context;
        mCameraManager = cameraManager;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getIconResId(int index) {
        return 0;//ICONS[0]
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Button button = new Button(mContext);
        button.setText(CONTENT[position]);
        button.setGravity(Gravity.CENTER);
        container.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraManager.switchCamera();
            }
        });
        return button;
    }
}
