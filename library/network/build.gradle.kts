plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.library.network"

    buildTypes {
        release {
            isMinifyEnabled
        }
    }
}

dependencies {
    implementation(libs.kotlin.coroutine.core)

    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
}