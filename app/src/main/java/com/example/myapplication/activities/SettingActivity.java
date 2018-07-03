package com.example.myapplication.activities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.myapplication.R;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.prefs_content, new SettingsFragment())
                .commit();
    }
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
            ListPreference list = (ListPreference) findPreference("languages");
            ListPreference list2 = (ListPreference) findPreference("colors");
            ListPreference list3 = (ListPreference) findPreference("database");
            Configuration configuration = new Configuration();
            getActivity().setTitle(getString(R.string.action_settings));

            list.setSummary(list.getValue());
            list2.setSummary(list2.getEntry());
            list3.setSummary(list3.getValue());

            list.setOnPreferenceChangeListener((preference, o) -> {
                list.setSummary(o.toString());
                if (o.toString().equals("Русский")){
                    Locale loc = new Locale("ru");
                    configuration.setLocale(loc);
                }
                if (o.toString().equals("English")){
                    Locale loc = new Locale("en");
                    configuration.setLocale(loc);
                }

                getActivity().getBaseContext().getResources().updateConfiguration(configuration,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                getActivity().recreate();
                return true;
            });

            list2.setOnPreferenceChangeListener((preference, o) -> {
                list2.setSummary(o.toString());
                list2.setValue(o.toString());
                getActivity().recreate();
                return false;
            });

            list3.setOnPreferenceChangeListener((preference, o) -> {
                list3.setSummary(o.toString());
                return true;
            });
        }
    }
}
