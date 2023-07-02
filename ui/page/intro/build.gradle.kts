plugins {
    id("beep.android.library.compose")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.page.intro"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)

    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(projects.ui.designsystem.dotindicator)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil)
    implementation(libs.coil.svg)
    implementation(libs.coil.compose)

    implementation(libs.airbnb.lottie)
    implementation(libs.airbnb.lottie.compose)
}
