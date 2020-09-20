package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.settings_pref);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for(int i = 0; i < count; i++){
            Preference pref = preferenceScreen.getPreference(i);
            if(pref != null && !(pref instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(pref.getKey(), "");
                if(!value.equals("")){
                    setPreferenceSummary(pref, value);
                }
            }
        }

    }

    private void setPreferenceSummary(Preference pref, String value){
        if(pref instanceof ListPreference){
            ListPreference listPref = (ListPreference) pref;
            int index = listPref.findIndexOfValue(value);
            if (index >= 0){
                listPref.setSummary(listPref.getEntries()[index]);
            }
        }else if(pref instanceof EditTextPreference){
            pref.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if(pref != null){
            String value = sharedPreferences.getString(key, "");
            if(!value.equals("")){
                setPreferenceSummary(pref, value);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
