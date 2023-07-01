import com.android.build.api.dsl.ApplicationExtension
import com.lighthouse.convention.ProjectConfigurations
import com.lighthouse.convention.configureAndroidCompose
import com.lighthouse.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UNUSED")
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
                configureKotlinAndroid(this)
                defaultConfig {
                    targetSdk = ProjectConfigurations.targetSdk
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }

                packaging {
                    resources.excludes.add("META-INF/LICENSE*")
                }
            }
        }
    }
}
