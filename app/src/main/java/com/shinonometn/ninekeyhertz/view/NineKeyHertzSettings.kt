package com.shinonometn.ninekeyhertz.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shinonometn.ninekeyhertz.R
import com.shinonometn.ninekeyhertz.service.HertzAccessibilityService
import kotlinx.android.synthetic.main.nine_key_hertz_settings.*

class NineKeyHertzSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nine_key_hertz_settings)

        main_hertzEnableLabel.text = if (HertzAccessibilityService.isAccessibilitySettingsOn(applicationContext))
            getString(R.string.settings_hertz_on)
        else
            getString(R.string.settings_hertz_off)

        Log.d(TAG,"Open settings")
        Log.d(TAG, "Files: (in ${filesDir.absolutePath}) \n ${StringBuilder().apply {
            filesDir.list().forEach { append(" - ").append(it).append("\n") }
        }}")

    }

    companion object {
        private const val TAG ="HertzSetting"
    }

}