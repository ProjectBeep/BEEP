plugins {
    id("beep.android.data")
}

android {
    namespace = "com.lighthouse.beep.data.encryptedpreference"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data.data)

    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.javax.inject)
}
