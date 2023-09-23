import com.android.build.gradle.LibraryExtension
import com.lighthouse.beep.convention.ProjectConfigurations
import com.lighthouse.beep.convention.configureKotlinAndroid
import com.lighthouse.beep.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("UNUSED")
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = ProjectConfigurations.targetSdk

                viewBinding.enable = true
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-activity-ktx").get())
                "implementation"(libs.findLibrary("androidx-appcompat").get())
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-constraintlayout").get())
                "implementation"(libs.findLibrary("androidx-recyclerview").get())
                "implementation"(libs.findLibrary("androidx-fragment-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}
