plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lighthouse.features.intro"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.common)
    implementation(projects.commonAndroid)
    implementation(projects.auth)
    implementation(projects.uiCommon)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.core.splashscreen)
    implementation(libs.androidX.appcompat)
    implementation(libs.androidX.constraintlayout)
    implementation(libs.androidX.fragment.ktx)

    implementation(libs.material)

    implementation(libs.gms.play.services.auth)

    implementation(libs.timber)
}
