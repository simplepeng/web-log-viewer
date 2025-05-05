package simple.library.weblogviewer

val Platform.isWeb: Boolean
    get() = getPlatform().name == "Web with Kotlin/Wasm"

