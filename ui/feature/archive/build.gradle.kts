plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.archive"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.model)
    implementation(projects.data.data)
    implementation(projects.auth)

    implementation(projects.navs)

    implementation(projects.ui.dialog.gifticondetail)
    implementation(projects.ui.dialog.confirmation)

    implementation(projects.ui.designsystem.snackbar)

    implementation(libs.glide)
}
