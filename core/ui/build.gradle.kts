plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.core.ui"

    viewBinding.enable = true
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
}
