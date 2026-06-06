// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // build-logic/convention 의 calmong.* 플러그인이 compileOnly로 참조 → 여기서 classpath에 노출
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.detekt) apply false
}