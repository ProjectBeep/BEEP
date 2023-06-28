plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine.core)
}
