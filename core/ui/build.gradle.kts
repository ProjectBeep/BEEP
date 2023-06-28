plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.core.ui"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
}
