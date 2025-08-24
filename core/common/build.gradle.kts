plugins {
    id("beep.android.library")
}

android {
    namespace = "com.lighthouse.beep.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine.core)
    
    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
