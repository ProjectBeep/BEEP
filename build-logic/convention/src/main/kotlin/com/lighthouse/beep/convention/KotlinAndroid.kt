package com.lighthouse.beep.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = ProjectConfigurations.compileSdk

        defaultConfig {
            minSdk = ProjectConfigurations.minSdk

            // Vector Drawable 을 사용하기 위한 설정
            // vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = ProjectConfigurations.javaVer
            targetCompatibility = ProjectConfigurations.javaVer

            // Java 에서 지원하는 라이브러리를 Android API 버전때문에 사용하지 못하는 경우
            // 사용하기 위한 설정
            // isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin()
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = ProjectConfigurations.javaVer.toString()

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-Xextended-compiler-checks"
            )
        }
    }
}
