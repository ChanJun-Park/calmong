plugins {
    id("calmong.android.library.compose")
}

android {
    namespace = "com.jingom.calmong.core.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.material3)
}
