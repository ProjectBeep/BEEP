plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.library.textformat"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)

    implementation(libs.androidx.core.ktx)
}
