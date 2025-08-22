package com.lighthouse.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = ProjectConfigurations.compileSdk

        defaultConfig {
            minSdk = ProjectConfigurations.minSdk

            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = ProjectConfigurations.javaVer
            targetCompatibility = ProjectConfigurations.javaVer
        }

        // Kotlin 컴파일러 옵션은 tasks에서 직접 설정

        viewBinding.enable = true
    }
}

internal fun Project.configureKotlinCompilerOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(ProjectConfigurations.javaVer.toString()))
        }
    }
}

@Suppress("RemoveRedundantBackticks")
internal fun Project.`kapt`(
    configure: Action<KaptExtension>,
) {
    (this as ExtensionAware).extensions.configure("kapt", configure)
}
