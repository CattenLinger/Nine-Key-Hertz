package net.catten.ninekeyhertz.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.*
import net.catten.ninekeyhertz.service.HertzAccessibilityService
import net.catten.ninekeyhertz.R
import net.catten.ninekeyhertz.databinding.NineKeyHertzSettingsBinding
import kotlin.coroutines.coroutineContext

class NineKeyHertzSettings : AppCompatActivity() {
    private val isAccessibilityEnabled : Boolean
        get() = HertzAccessibilityService.isAccessibilitySettingsOn(applicationContext)

    private val binding by lazy { NineKeyHertzSettingsBinding.inflate(layoutInflater) }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onDestroy() {
        scope.cancel(CancellationException("Activity destroying"))
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        scope.launch {
            when(isAccessibilityEnabled) {
                true -> getString(R.string.settings_hertz_on)
                else -> getString(R.string.settings_hertz_off)
            }.let { withContext(Dispatchers.Main) { binding.mainHertzEnableLabel.text = it } }

            Log.d(TAG,"Open settings")
            Log.d(TAG, "Files: (in ${filesDir.absolutePath}) \n ${StringBuilder().apply {
                filesDir.list()?.forEach { append(" - ").append(it).append("\n") }
            }}")
        }
    }

    companion object {
        private const val TAG ="HertzSetting"
    }

}