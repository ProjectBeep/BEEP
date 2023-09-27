plugins {
    id("beep.android.feature")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.confirmation"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(libs.androidx.fragment.ktx)
}
