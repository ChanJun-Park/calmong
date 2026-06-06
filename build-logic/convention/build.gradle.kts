plugins {
    `kotlin-dsl`
}

group = "com.jingom.calmong.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("spotless") {
            id = "calmong.spotless"
            implementationClass = "calmong.SpotlessConventionPlugin"
        }
        register("detekt") {
            id = "calmong.detekt"
            implementationClass = "calmong.DetektConventionPlugin"
        }
        register("androidLibrary") {
            id = "calmong.android.library"
            implementationClass = "calmong.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "calmong.android.library.compose"
            implementationClass = "calmong.AndroidLibraryComposeConventionPlugin"
        }
    }
}
