plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

// @Suppress("UnstableApiUsage")
android {
    namespace = "com.lighthouse.beep.ui.page.intro"
}

dependencies {
    implementation(projects.ui.core)
    implementation(projects.theme)

    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
