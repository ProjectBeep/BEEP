plugins {
    id("beep.android.library")
    id("beep.android.library.compose")
}

android {
    namespace = "com.lighthouse.beep.core.ui"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.compose.ui)
}
