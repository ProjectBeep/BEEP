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
    implementation(projects.data.data)
    implementation(projects.domain)

    implementation(projects.navs)
    implementation(projects.permission)

    implementation(projects.ui.dialog.bottomsheet)
    implementation(projects.ui.dialog.progress)
    implementation(projects.ui.dialog.confirmation)
    implementation(projects.ui.dialog.textinput)
    implementation(projects.ui.dialog.datepicker)
    implementation(projects.ui.designsystem.cropview)
    implementation(projects.ui.designsystem.snackbar)

    implementation(projects.library.barcode)
    implementation(projects.library.recognizer)

    implementation(libs.glide)

    implementation(libs.squareup.moshi.kotlin)
}
