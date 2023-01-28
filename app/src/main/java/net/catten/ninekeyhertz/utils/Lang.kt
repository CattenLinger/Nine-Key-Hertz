package net.catten.ninekeyhertz.utils

fun <T> tryGetOrNull(whenError: (Exception) -> T? = { null }, provider : () -> T) : T? = try {
    provider()
} catch (e : Exception) {
    whenError(e)
}