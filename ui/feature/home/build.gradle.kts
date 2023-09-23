plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.home"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(projects.navs)

    implementation(libs.coil)
    implementation(libs.coil.svg)
}
