package com.example.myapplication.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

import com.example.myapplication.R

import java.util.Locale

class SettingActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.prefs_content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        /**
         * Called during [.onCreate] to supply the preferences for this fragment.
         * Subclasses are expected to call [.setPreferenceScreen] either
         * directly or via helper methods such as [.addPreferencesFromResource].
         *
         * @param savedInstanceState If the fragment is being re-created from
         * a previous saved state, this is the state.
         * @param rootKey If non-null, this preference fragment should be rooted at the
         * [androidx.preference.PreferenceScreen] with this key.
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings, rootKey)

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onResume() {
            super.onResume()
            val list = findPreference("languages")
            val list2 = findPreference("colors")
            val list3 = findPreference("database")
            val configuration = Configuration()
            activity!!.title = getString(R.string.action_settings)

            list.summary = list.sharedPreferences.getString(list.key, "")
            list2.summary = list2.sharedPreferences.getString(list2.key, "")
            list3.summary = list3.sharedPreferences.getString(list3.key, "")

            list.setOnPreferenceChangeListener { _, o ->
                if (o.toString() == "Русский") {
                    val loc = Locale("ru")
                    configuration.setLocale(loc)
                }
                if (o.toString() == "English") {
                    val loc = Locale("en")
                    configuration.setLocale(loc)
                }

                activity!!.baseContext.createConfigurationContext(configuration)
                activity!!.recreate()
                true
            }

            list2.setOnPreferenceChangeListener { _, o ->
                activity!!.recreate()
                true
            }

            list3.setOnPreferenceChangeListener { _, o ->
                activity!!.recreate()
                true
            }
        }
    }
}
