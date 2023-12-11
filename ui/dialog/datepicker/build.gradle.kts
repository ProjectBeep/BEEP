plugins {
    id("beep.android.feature")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.datepicker"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.theme)
    implementation(projects.ui.dialog.bottomsheet)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.android.material)
}
