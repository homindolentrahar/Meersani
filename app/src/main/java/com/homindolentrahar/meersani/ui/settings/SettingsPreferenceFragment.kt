package com.homindolentrahar.meersani.ui.settings


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.service.AlarmReceiver
import com.homindolentrahar.meersani.ui.main.MainActivity
import com.homindolentrahar.meersani.util.LocaleManager

/**
 * A simple [Fragment] subclass.
 */
class SettingsPreferenceFragment :
    PreferenceFragmentCompat() {
    private lateinit var switchDailyReminder: SwitchPreference
    private lateinit var switchTodayRelease: SwitchPreference
    private lateinit var listLanguage: ListPreference
    private val alarmReceiver = AlarmReceiver()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
        init()
        onStateChange()
    }

    private fun init() {
        switchDailyReminder = findPreference<SwitchPreference>(getString(R.string.daily_reminder))!!
        switchTodayRelease = findPreference<SwitchPreference>(getString(R.string.today_release))!!
        listLanguage = findPreference<ListPreference>(getString(R.string.change_language))!!
    }

    private fun onStateChange() {
//        Daily Reminder
        switchDailyReminder.setOnPreferenceChangeListener { preference, newValue ->
            if (switchDailyReminder.isChecked) {
                Toast.makeText(context!!, "Disabled !", Toast.LENGTH_SHORT).show()
                alarmReceiver.cancelReminder(context!!, AlarmReceiver.TYPE_DAILY_REMINDER)
            } else {
                Toast.makeText(context!!, "Enabled !", Toast.LENGTH_SHORT).show()
                alarmReceiver.setupDailyReminder(context!!)
            }
            true
        }
//        Today Released Movies
        switchTodayRelease.setOnPreferenceChangeListener { preference, newValue ->
            if (switchTodayRelease.isChecked) {
                Toast.makeText(context!!, "Disabled !", Toast.LENGTH_SHORT).show()
                alarmReceiver.cancelReminder(context!!, AlarmReceiver.TYPE_TODAY_RELEASE)
            } else {
                Toast.makeText(context!!, "Enabled !", Toast.LENGTH_SHORT).show()
                alarmReceiver.setupTodayRelease(context!!)
            }
            true
        }
//        Change Language
        listLanguage.setOnPreferenceChangeListener { preference, newValue ->
            LocaleManager.setNewLocale(context!!, newValue.toString())
            startActivity(Intent(context!!, MainActivity::class.java))
            activity?.finish()
            true
        }
    }
}
