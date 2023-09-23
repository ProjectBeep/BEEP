plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.main"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(libs.coil)
    implementation(libs.coil.svg)
}
