plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.designsystem.cropview"
}

dependencies {
    implementation(projects.core.common)

    implementation(projects.theme)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine.core)
}
