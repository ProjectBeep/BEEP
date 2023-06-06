plugins {
    id("beep.android.page")
}

// @Suppress("UnstableApiUsage")
android {
    namespace = "com.lighthouse.beep.ui.page.intro"
}

dependencies {
//    implementation(projects.uiCommon)

    implementation(projects.auth.google)
    implementation(projects.auth.kakao)
    implementation(projects.auth.naver)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
