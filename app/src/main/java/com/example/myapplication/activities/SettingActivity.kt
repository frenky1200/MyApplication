package com.example.myapplication.activities

import android.content.res.Configuration
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity

import com.example.myapplication.R

import java.util.Locale

class SettingActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        fragmentManager
                .beginTransaction()
                .add(R.id.prefs_content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.settings)

        }

        override fun onResume() {
            super.onResume()
            val list = findPreference("languages") as ListPreference
            val list2 = findPreference("colors") as ListPreference
            val list3 = findPreference("database") as ListPreference
            val configuration = Configuration()
            activity.title = getString(R.string.action_settings)

            list.summary = list.value
            list2.summary = list2.entry
            list3.summary = list3.entry

            list.setOnPreferenceChangeListener { preference, o ->
                list.summary = o.toString()
                if (o.toString() == "Русский") {
                    val loc = Locale("ru")
                    configuration.setLocale(loc)
                }
                if (o.toString() == "English") {
                    val loc = Locale("en")
                    configuration.setLocale(loc)
                }

                activity.baseContext.resources.updateConfiguration(configuration,
                        activity.baseContext.resources.displayMetrics)
                activity.recreate()
                true
            }

            list2.setOnPreferenceChangeListener { preference, o ->
                list2.summary = o.toString()
                list2.value = o.toString()
                activity.recreate()
                false
            }

            list3.setOnPreferenceChangeListener { preference, o ->
                list3.summary = o.toString()
                list3.value = o.toString()
                activity.recreate()
                false
            }
        }
    }
}
