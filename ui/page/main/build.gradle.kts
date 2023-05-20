plugins {
    id("beep.android.page")
}

android {
    namespace = "com.lighthouse.beep.ui.page.main"
}

dependencies {
//    implementation(projects.navApp)
//    implementation(projects.navMain)
//    implementation(projects.uiGifticonlist)
//    implementation(projects.uiHome)
//    implementation(projects.uiSetting)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
