
import com.lighthouse.convention.implementation
import com.lighthouse.convention.kapt
import com.lighthouse.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UNUSED")
class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                implementation(libs.findLibrary("dagger-hilt-android"))
                kapt(libs.findLibrary("dagger-hilt-android-compiler"))
            }

            kapt {
                correctErrorTypes = true
            }
        }
    }
}
