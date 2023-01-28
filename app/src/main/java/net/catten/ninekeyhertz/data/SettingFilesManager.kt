package net.catten.ninekeyhertz.data

import android.content.Context
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

object SettingFilesManager {
    // Configure the jackson json mapper here
    val jsonMapper = ObjectMapper()
}

inline fun <reified T : NamedSettings> SettingFilesManager.load(context: Context): T {
    val test = T::class.java.newInstance()
    return File(context.filesDir, "${test.name}.json").takeIf { it.isFile }?.let {
        jsonMapper.readerFor(object : TypeReference<T>() {}).readValue(it)
    } ?: test
}

interface NamedSettings {
    @get:JsonIgnore
    val name: String
}

fun NamedSettings.saveToFile(context: Context) {
    SettingFilesManager.jsonMapper.writeValue(File(context.filesDir, "$name.json"), this)
}