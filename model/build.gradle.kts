plugins {
    id("beep.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lighthouse.beep.model"
}

dependencies {
    implementation(projects.library.textformat)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.datetime)

    implementation(libs.androidx.recyclerview)

    implementation(libs.squareup.moshi.kotlin)
}
