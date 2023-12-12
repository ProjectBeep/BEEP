plugins {
    id("beep.android.library")
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.lighthouse.beep.core.ui"

    viewBinding.enable = true
}

dependencies {
    implementation(libs.android.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
}
