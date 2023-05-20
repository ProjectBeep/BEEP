plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.library.recognizer"
}

dependencies {
    implementation(libs.kotlin.coroutine.core)

    implementation(libs.mlkit.text.recognition.korean)
}
