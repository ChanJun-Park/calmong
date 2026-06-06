import com.android.build.api.dsl.ApplicationExtension
import com.jingom.calmong.configureKotlinAndroid
import com.jingom.calmong.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            // Kotlin Android plugin은 AGP 9부터 내장되어 명시 적용 금지
            // (https://kotl.in/gradle/agp-built-in-kotlin)
            apply("calmong.spotless")
            apply("calmong.detekt")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            defaultConfig.targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
            defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
