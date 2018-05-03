package com.example.enjoy.enjoycamera.Utils;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 2018/05/02   .
 */

public class ImageSaveTask extends AsyncTask<Void,Void,String> {

    private final byte[] data;

    public ImageSaveTask(byte[] data) {
        this.data = data;
    }

    @Override
    protected void onPostExecute(String filePath) {
        super.onPostExecute(filePath);
        if(filePath!=null){
            EventBus.getDefault().post(new MessageEvent(filePath));
        }
    }

    @Override
    protected String doInBackground(Void[] voids) {
        File pictureFile = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return null;
        }
        String filePath = pictureFile.getAbsolutePath();
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
