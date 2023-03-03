plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lighthouse.auth.google"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.common)
    implementation(projects.commonAndroid)
    implementation(projects.auth)

    implementation(libs.androidX.fragment.ktx)

    implementation(libs.gms.play.services.auth)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
