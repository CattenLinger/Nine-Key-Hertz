package com.shinonometn.ninekeyhertz.data

import android.content.Context
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

object SettingFilesManager {

    // Configure the jackson json mapper here
    val jsonMapper = ObjectMapper()

    private val clazzMapping = HashMap<String,Class<NamedSettings>>()
}

fun <T : NamedSettings> SettingFilesManager.loadSettings(context: Context, clazz: Class<T>): T {
    val test = clazz.newInstance()
    return jsonMapper.readerFor(object : TypeReference<T>() {}).readValue(File(context.filesDir, "${test.name}.json"))
}

interface NamedSettings {
    @get:JsonIgnore
    val name: String
}

fun NamedSettings.saveToFile(context: Context) {
    SettingFilesManager.jsonMapper.writeValue(File(context.filesDir, "$name.json"), this)
}