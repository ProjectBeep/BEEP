plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.worker"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.commonAndroid)
    implementation(projects.domain)
    implementation(projects.uiCommon)

    implementation(libs.androidX.hilt.work)
    implementation(libs.androidX.work.runtime.ktx)

    implementation(libs.timber)
}
