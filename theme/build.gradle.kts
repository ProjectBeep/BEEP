plugins {
    id("beep.android.library")
    id("beep.android.library.compose")
}

android {
    namespace = "com.lighthouse.beep.theme"
}

dependencies {
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
}
