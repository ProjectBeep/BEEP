plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.confirmation"

    buildFeatures.viewBinding = true
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
}
