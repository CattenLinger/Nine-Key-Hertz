package net.catten.ninekeyhertz.utils

import android.content.Context

fun <T> tryGetOrNull(whenError: (Exception) -> T? = { null }, provider : () -> T) : T? = try {
    provider()
} catch (e : Exception) {
    whenError(e)
}

inline fun <reified T> classNameQualified() : String = T::class.qualifiedName ?: error("The class has no name")

inline fun <reified T> classNameSimple() : String = T::class.simpleName ?: error("The class has no name")

/**
 * Get the identifier of a class.
 * Useful when needs to get an canonical name of some component in app
 */
inline fun <reified T> Context.identifierOfClazz() : String = packageName + "/" + T::class.qualifiedName