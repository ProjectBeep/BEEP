plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.originimage"
}

dependencies {
    implementation(projects.ui.core)
    implementation(projects.core)

    implementation(libs.coil)

    implementation(libs.androidx.fragment.ktx)
}
