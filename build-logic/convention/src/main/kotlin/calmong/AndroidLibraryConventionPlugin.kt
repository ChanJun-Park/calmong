package calmong

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            // Kotlin Android plugin은 AGP 9부터 내장되어 명시 적용 금지
            // (https://kotl.in/gradle/agp-built-in-kotlin)
            apply("calmong.spotless")
            apply("calmong.detekt")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        val compileSdkMajor = libs.findVersion("compileSdk").get().requiredVersion.toInt()
        val compileSdkMinor = libs.findVersion("compileSdkMinor").get().requiredVersion.toInt()
        val minSdkVer = libs.findVersion("minSdk").get().requiredVersion.toInt()
        val javaVer = JavaVersion.toVersion(libs.findVersion("javaVersion").get().requiredVersion)

        extensions.configure<LibraryExtension> {
            compileSdk {
                version =
                    release(compileSdkMajor) {
                        minorApiLevel = compileSdkMinor
                    }
            }
            defaultConfig {
                minSdk = minSdkVer
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility = javaVer
                targetCompatibility = javaVer
            }
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVer.toString()))
            }
        }
    }
}
