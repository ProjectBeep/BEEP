plugins {
    id("beep.android.library")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.auth"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.domain)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
}