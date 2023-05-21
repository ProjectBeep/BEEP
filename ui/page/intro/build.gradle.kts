plugins {
    id("beep.android.page")
}

android {
    namespace = "com.lighthouse.beep.ui.page.intro"
}

dependencies {
//    implementation(projects.authGoogle)
//    implementation(projects.uiCommon)

    implementation(projects.ui.dialog.progress)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
