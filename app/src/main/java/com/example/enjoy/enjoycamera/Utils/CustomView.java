package com.example.enjoy.enjoycamera.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.example.enjoy.enjoycamera.CameraPreview;

/**
 * Created by libo on 2018/3/23.
 */

public class CustomView extends View {
    private static final String TAG = "CustomView";
    private int mLineX = 2;
    private int mLineY = 2;
    private int mWidth = 1920;
    private int mHeight = 1080;
    private Paint mPaint = null;

    public CustomView(Context context) {
        super(context);
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
/*        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();*//*
        setMeasuredDimension(mWidth,mHeight);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        mWidth = CameraPreview.sSurfaceViewW;
        mHeight = CameraPreview.sSurfaceViewH;
        int x = mWidth/(mLineX+1);
        int y = mHeight/(mLineY+1);
        Log.d(TAG,"on draw width = "+mWidth + "heigth = "+mHeight);
        for(int i =1; i<=mLineX;i++){
            canvas.drawLine(x * i, 0, x * i, mHeight, mPaint);
        }

        for (int j =1;j<=mLineY;j++){
            canvas.drawLine(0, y * j, mWidth, y *j, mPaint);
        }
    }
}
