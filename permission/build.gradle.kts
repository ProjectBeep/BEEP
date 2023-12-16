plugins {
    id("beep.android.feature")
}

android {
    namespace = "com.lighthouse.beep.permission"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(projects.ui.dialog.confirmation)

    implementation(libs.androidx.fragment.ktx)
}
