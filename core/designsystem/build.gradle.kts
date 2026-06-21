plugins {
    id("calmong.android.library")
    id("calmong.android.library.compose")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.jingom.calmong.core.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)

    testImplementation(libs.junit)
}
