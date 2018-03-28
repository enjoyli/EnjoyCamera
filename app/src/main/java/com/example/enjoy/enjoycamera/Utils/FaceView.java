package com.example.enjoy.enjoycamera.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;

import com.example.enjoy.enjoycamera.CameraPreview;
import com.example.enjoy.enjoycamera.R;

/**
 * Created by admin on 2018/03/28   .
 */

public class FaceView extends View {
    private static final String TAG = "FaceView";
    private Camera.Face[] mFaces;
    private Drawable mFaceIndicator = null;
    private Paint mPaint;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = new RectF();

    public FaceView(Context context) {
        super(context);
        mFaceIndicator = context.getResources().getDrawable(R.drawable.ic_face_detection_focusing);
    }

    public void setFace(Camera.Face[] faces){
        mFaces = faces;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mFaces == null || mFaces.length < 1){
            return;
        }
        Log.d(TAG,"H ="+ getHeight());
        MatrixUtil.prepareMatrix(mMatrix,false,90,
                CameraPreview.sSurfaceViewW,CameraPreview.sSurfaceViewH);
        canvas.save();
        mMatrix.postRotate(0);
        canvas.rotate(-0);
        for(int i =0;i<mFaces.length;i++){
            mRect.set(mFaces[i].rect);
            mMatrix.mapRect(mRect);
            mFaceIndicator.setBounds(Math.round(mRect.left),
                    Math.round(mRect.top),
                    Math.round(mRect.right),
                    Math.round(mRect.bottom));
            mFaceIndicator.draw(canvas);
        }
        canvas.restore();

    }
}
