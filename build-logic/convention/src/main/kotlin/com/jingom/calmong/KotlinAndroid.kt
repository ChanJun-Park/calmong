package com.jingom.calmong

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    val compileSdkMajor = libs.findVersion("compileSdk").get().requiredVersion.toInt()
    val compileSdkMinor = libs.findVersion("compileSdkMinor").get().requiredVersion.toInt()
    val minSdkVer = libs.findVersion("minSdk").get().requiredVersion.toInt()
    val javaVer = JavaVersion.toVersion(libs.findVersion("javaVersion").get().requiredVersion)

    commonExtension.apply {
        compileSdk {
            version =
                release(compileSdkMajor) {
                    minorApiLevel = compileSdkMinor
                }
        }
        defaultConfig.apply {
            minSdk = minSdkVer
        }
        compileOptions.apply {
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
