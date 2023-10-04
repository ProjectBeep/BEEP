plugins {
    id("beep.android.feature")
    id("beep.android.hilt")
}

android {
    namespace = "com.lighthouse.beep.ui.feature.editor"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)
    implementation(projects.domain)

    implementation(projects.navs)

    implementation(projects.ui.dialog.progress)
    implementation(projects.ui.dialog.confirmation)
    implementation(projects.ui.dialog.textinput)
    implementation(projects.ui.designsystem.cropview)

    implementation(projects.library.recognizer)

    implementation(libs.coil)

    implementation(libs.zxing.core)
    implementation(libs.squareup.moshi.kotlin)
}
