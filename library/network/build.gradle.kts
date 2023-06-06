plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.library.network"
}

dependencies {
    implementation(libs.kotlin.coroutine.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
}