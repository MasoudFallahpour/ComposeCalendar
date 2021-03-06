object Dependencies {

    object App {
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
        const val core = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0"
        const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha03"
        const val material = "com.google.android.material:material:1.3.0"

        const val composeUi = "androidx.compose.ui:ui:${Versions.composeVersion}"
        const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
        const val composeMaterial = "androidx.compose.material:material:${Versions.composeVersion}"
        const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.composeVersion}"
    }

}