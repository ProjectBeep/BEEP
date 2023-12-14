plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.designsystem.dotindicator"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)

    implementation(projects.theme)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.recyclerview)
}
