plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.features.setting"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.common)
    implementation(projects.commonAndroid)
    implementation(projects.authGoogle)
    implementation(projects.uiCommon)
    implementation(projects.navApp)
    implementation(projects.navMain)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.appcompat)
    implementation(libs.androidX.constraintlayout)
    implementation(libs.androidX.fragment.ktx)
    implementation(libs.androidX.navigation.fragment.ktx)
    implementation(libs.androidX.navigation.ui.ktx)

    implementation(libs.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    implementation(libs.timber)
}
