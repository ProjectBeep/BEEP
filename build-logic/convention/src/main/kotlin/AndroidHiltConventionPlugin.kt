import com.lighthouse.beep.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

@Suppress("UNUSED")
class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                "implementation"(libs.findLibrary("dagger-hilt-android").get())
                "implementation"(libs.findLibrary("javax-inject").get())
                "kapt"(libs.findLibrary("dagger-hilt-android-compiler").get())
            }

            extensions.configure<KaptExtension> {
                correctErrorTypes = true
            }
        }
    }
}
