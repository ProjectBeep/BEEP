plugins {
    id("beep.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lighthouse.beep.model"
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.datetime)

    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.squareup.moshi.kotlin.codegen)
}
