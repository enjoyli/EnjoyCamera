package com.example.enjoy.enjoycamera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by admin on 2018/03/22   .
 */

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
        preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        preference.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        preference.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if("list_preference_preview_size".equals(key)){
            Preference pref = findPreference(key);
            pref.setSummary(preference.getString(key,"0"));
        }else if("switch_preference_shutter_sound".equals(key)){

        }

    }
}
