plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.progress"
}

dependencies {
    implementation(projects.ui.core)

    implementation(libs.androidx.fragment.ktx)
}
