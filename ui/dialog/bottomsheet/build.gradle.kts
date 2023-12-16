plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.bottomsheet"

    buildFeatures.viewBinding = true
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.transition.ktx)
    implementation(libs.androidx.constraintlayout)
}
