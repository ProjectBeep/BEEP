plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.library.barcode"
}

dependencies {
    implementation(libs.zxing.core)
}
