plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.setting"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.data.data)

    implementation(projects.auth)

    implementation(projects.navs)

    implementation(projects.ui.dialog.confirmation)
    implementation(projects.ui.dialog.withdrawal)

    implementation(libs.glide)
    implementation(libs.gms.play.services.oss.licences)
}
