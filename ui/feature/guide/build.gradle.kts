plugins {
    id("beep.android.library.compose")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.guide"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil)
    implementation(libs.coil.svg)
    implementation(libs.coil.compose)
}
