package com.example.enjoy.enjoycamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.enjoy.enjoycamera.Utils.permissionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    String mCurrentPhotoPath;
    private ImageView mImageView = null;
    private Button mButton = null;
    private VideoView mVideoView = null;
    private List<String> permissionsList = new ArrayList<String>();
    private permissionManager mPermissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionManager = new permissionManager(this);
        if(mPermissionManager.requestPermissions()){
            initView();
        }
        //requestPermission();
    }

    private void initView(){
        mImageView = findViewById(R.id.imageView);
        mVideoView = findViewById(R.id.videoView);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakeVideoIntent();
                //mCamera.takePicture(null,null,mPicture);
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile!=null) {
                String authority = getPackageName() + ".fileprovider";
                Uri photoURI = FileProvider.getUriForFile(this,authority,photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
            }
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    private  void dispatchTakeVideoIntent(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
/*            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            mImageView.setImageBitmap(imageBitmap);*/
            //setPic();
            //galleryAddPic();
            Uri uri = data.getData();
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }
    }

    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File image = null;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
             image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void setPic() {
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private boolean addPermission(List<String> permissionsList,String permission){
        if(ContextCompat.checkSelfPermission(this,permission)
                != PackageManager.PERMISSION_GRANTED){
            permissionsList.add(permission);

            return false;
        }
        return true;
    }
    private void requestPermission(){
        List<String> permissionsNeeded = new ArrayList<String>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }else {
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case permissionManager.REQUEST_PERMISSIONS:
                if(grantResults.length>0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initView();
                }else {

                }
                return;
        }
    }
}
