plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.core"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine.core)
}
