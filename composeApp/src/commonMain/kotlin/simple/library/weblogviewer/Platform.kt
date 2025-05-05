package simple.library.weblogviewer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

val isWeb: Boolean
    get() = getPlatform().isWeb