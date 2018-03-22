package com.example.enjoy.enjoycamera;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

/*        fragmentManager = getFragmentManager();
        settingFragment = new SettingFragment();
        fragmentManager.beginTransaction().add(R.id.fragment,settingFragment).commit();*/
    }
}
