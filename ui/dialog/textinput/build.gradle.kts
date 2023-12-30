plugins {
    id("beep.android.feature")
}

android {
    namespace = "com.lighthouse.beep.ui.dialog.textinput"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.theme)

    implementation(projects.library.textformat)

    implementation(libs.androidx.fragment.ktx)
}
