package com.example.enjoy.enjoycamera.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/03/20   .
 */

public class PermissionManager {
    public static final int REQUEST_PERMISSIONS = 100;
    private List<String> mPermissionsList = new ArrayList<String>();
    private Activity mActivity;

    public PermissionManager(Activity activity) {
        mActivity = activity;

        initPermissionsList();
    }

    private void initPermissionsList(){
        mPermissionsList.add(Manifest.permission.CAMERA);
        mPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private List<String> getNeedCheckPermissionList(List<String> permissionsList){
        List<String> needCheckPermissionList = new ArrayList<String>();
        for(String permission:permissionsList){
            if(ContextCompat.checkSelfPermission(mActivity,permission)
                    != PackageManager.PERMISSION_GRANTED){
                needCheckPermissionList.add(permission);
            }
        }
        return needCheckPermissionList;
    }

    public boolean requestPermissions(){
        List<String> needCheckPermissionsList = getNeedCheckPermissionList(mPermissionsList);
        if(needCheckPermissionsList.size() > 0) {
            ActivityCompat.requestPermissions(mActivity,
                    needCheckPermissionsList.toArray(new String[needCheckPermissionsList.size()]),
                            REQUEST_PERMISSIONS
                    );
            return false;
        }
        return true;
    }
}
