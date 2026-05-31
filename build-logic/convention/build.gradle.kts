plugins {
    `kotlin-dsl`
}

group = "com.jingom.calmong.buildlogic"

dependencies {
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
    }
}
