plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.features.common"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.common)
    implementation(projects.commonAndroid)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.constraintlayout)

    implementation(libs.material)

    implementation(libs.coil)
}
