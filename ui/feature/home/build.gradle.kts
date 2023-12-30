plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.home"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.model)
    implementation(projects.data.data)
    implementation(projects.auth)

    implementation(projects.permission)

    implementation(projects.ui.dialog.gifticondetail)
    implementation(projects.ui.dialog.confirmation)

    implementation(projects.ui.designsystem.dotindicator)
    implementation(projects.ui.designsystem.snackbar)

    implementation(projects.navs)

    implementation(libs.glide)
}
