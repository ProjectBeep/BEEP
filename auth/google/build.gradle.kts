plugins {
    id("beep.android.library")
    id("beep.android.hilt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lighthouse.beep.auth.google"
}

dependencies {
    implementation(projects.auth.auth)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.gms.play.services.auth)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}
